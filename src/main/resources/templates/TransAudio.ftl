<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>音频转写页面</title>
</head>
<body>
<div style="width:100%;text-align:center">
<p style="font-size:30px">音频转写页面</p>
<form action="/TransAudio" method="post">
    <p style="font-family:times;color:darkblue">引擎选择</p>
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
    <br><br>
    <input type="text" name="audiopath" value="C:\Users\kehu2\Desktop\test\">
    <br><br>
    <button type="submit">获取转写结果</button>
</form>
</div>
</body>
<script>


</script>
</html>
