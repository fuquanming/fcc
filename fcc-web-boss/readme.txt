@Cacheable(value = {"fcc:operateMapCache:0"}, key = "'fcc:operateMapCache'")
value值使用,分隔符:,最后一个为缓存时间，在Redis中使用，单位：秒