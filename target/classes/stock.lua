if redis.call('sismember', KEYS[2], ARGV[1]) == 1 then
    return -2
end
if (redis.call('exists', KEYS[1]) == 1) then
    local stock = tonumber(redis.call('get', KEYS[1]));
    if (stock > 0) then
        redis.call('incrby', KEYS[1], -1);
        redis.call('sadd', KEYS[2], ARGV[1])
        if redis.call('ttl', KEYS[2]) == -1 then
            redis.call('expire', KEYS[2], tonumber(ARGV[2]))
        end
        return stock-1;
    end;
     return -1;
end;
-- if (redis.call('exists', KEYS[1]) == 1) then
--     local stock = tonumber(redis.call('get', KEYS[1]));
--     if (stock > 0) then
--         redis.call('incrby', KEYS[1], -1);
--         return stock-1;
--     else
--         return -1;
--     end;
-- end;