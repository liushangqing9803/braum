local keyPoint = KEYS[1];
local stableIntervalMicrosParam = ARGV[1];
local maxPermitsParam = ARGV[2];
local ratelimitInfo = redis.pcall('HMGET', keyPoint, 'nextFreeTicketMicros', 'storedPermits');
local current = redis.call('TIME');
local nowMicros = current[1] * 1000000 + current[2];
local nextFreeTicketMicros = nowMicros
local storedPermits = 0
local maxPermits = tonumber(maxPermitsParam);
local stableIntervalMicros = tonumber(stableIntervalMicrosParam);
if ratelimitInfo[1] then
    nextFreeTicketMicros = tonumber(ratelimitInfo[1]);
else
    redis.pcall('HMSET', keyPoint, 'nextFreeTicketMicros', tostring(nextFreeTicketMicros));
end
if ratelimitInfo[2] then
    storedPermits = tonumber(ratelimitInfo[2]);
else
    redis.pcall('HMSET', keyPoint, 'storedPermits', tostring(storedPermits));
end
local spTime = nowMicros - nextFreeTicketMicros;
if spTime >= 0 then
    local newPermits = spTime / stableIntervalMicros;
    storedPermits = math.min(maxPermits, storedPermits + newPermits);
    local storedPermitsToSpend = math.min(1, storedPermits);
    local freshPermits = 1 - storedPermitsToSpend;
    local waitMicros = freshPermits * stableIntervalMicros;
    nextFreeTicketMicros = nowMicros + waitMicros;
    storedPermits = storedPermits - storedPermitsToSpend;
    redis.pcall('HMSET', keyPoint, 'nextFreeTicketMicros', tostring(nextFreeTicketMicros), 'storedPermits', tostring(storedPermits));
    redis.pcall('EXPIRE', keyPoint, 3600);
    return 1;
else
    if storedPermits - 1 >= 0 then
        return 1;
    else
        return -1;
    end
end
