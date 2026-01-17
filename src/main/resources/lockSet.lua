local key = KEYS[1]
local member = ARGV[1]

-- 检查元素是否存在
if redis.call('SISMEMBER', key, member) == 1 then
    -- 元素存在，返回1
    return 1
else
    -- 元素不存在，添加并返回2
    redis.call('SADD', key, member)
    return 2
end