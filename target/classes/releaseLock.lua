local key = KEYS[1]
local member = ARGV[1]

-- 执行移除操作，SREM返回被移除的元素数量
local removed = redis.call('SREM', key, member)