<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>效果测试页面(结果存储于不同文件)</title>
</head>
<body>
<div style="width:100%;text-align:center">
    <p style="font-size:30px">效果测试页面(结果存储于不同文件)</p>
    <form action="/CompareAudio" method="post">
        <p style="font-family:times;color:darkblue">转写引擎选择</p>
        <br><br>
        <select id="urlpath" name="urlpath">
            <#--value 指发送到服务器的值-->
            <option value="http://172.31.161.157:33721">离线转写引擎1075.7-GPU</option>
            <option value="http://172.31.161.96:33721">离线转写引擎1075.7-CPU</option>
            <option value="http://172.31.161.137:33721">ED-65-GPU</option>
            <option value="audi">待补充</option>
        </select>
        <br><br>
        <p style="font-family:times;color:darkblue">待转写音频文件夹选择</p>
        <input type="text" name="audiopath" value="C:\Users\kehu2\Desktop\test\">
        <br><br>
        <p style="font-family:times;color:darkblue">音频文件夹对应标注文件路径</p>
        <input type="text" name="tagpath" value="C:\Users\kehu2\Desktop\test标注\">
        <br><br>
        <button type="submit">获取对比结果</button>
    </form>

</div>
</body>
<script>


</script>
</html>
