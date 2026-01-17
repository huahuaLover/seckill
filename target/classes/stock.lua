-- if redis.call('exists', KEYS[2]) == 1 then
--     return -2  -- -2 表示重复下单
-- end
-- if (redis.call('exists', KEYS[1]) == 1) then
--     local stock = tonumber(redis.call('get', KEYS[1]));
--     if (stock > 0) then
--         redis.call('incrby', KEYS[1], -1);
--         redis.call('set', KEYS[2], 1, 'EX', 86400)
--         return stock-1;
--     end;
--      return -1;
-- end;
if (redis.call('exists', KEYS[1]) == 1) then
    local stock = tonumber(redis.call('get', KEYS[1]));
    if (stock > 0) then
        redis.call('incrby', KEYS[1], -1);
        return stock-1;
    else
        return -1;
    end;
end;