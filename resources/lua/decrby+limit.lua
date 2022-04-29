local value = redis.call("get", KEYS[1]) or 0
local decrement = tonumber(ARGV[1])
local limit = tonumber(ARGV[2])

if (value - decrement) < limit then
	value = redis.call("decrby", KEYS[1], value - limit)
else
	value = redis.call("decrby", KEYS[1], decrement)
end
return value
