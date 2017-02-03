@Cacheable(value = {"fcc:operateMapCache:0"}, key = "'fcc:operateMapCache'")
value值使用,分隔符:,最后一个为缓存时间，在Redis中使用，单位：秒

// 文件上传
<tool:fileUpload linkType="${linkType }" annexType="${annexType }" fileType="gif|jpeg|jpg|png"></tool:fileUpload>
// 文件显示
<tool:fileShow linkType="${linkType }" annexType="${annexType }" linkId="${data.userId }"></tool:fileShow>
// 缩略图 宽度、高度
manage/sys/sysAnnex/image.do?id=402881e959f934f10159f9f8bb2d003a&widht=100&height=100
// 缩略图 比例
manage/sys/sysAnnex/image.do?id=402881e959f934f10159f9f8bb2d003a&scale=0.5