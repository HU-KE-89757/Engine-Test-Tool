<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>效果测试工具-主页面</title>
</head>
<body>

<div style="width:100%;text-align:center">
    <p style="font-size:30px">效果测试工具</p>
<button onclick=trans() style="width:120px;height:60px">获取转写结果</button>
<br><br><br>
<button onclick=transSpk() style="width:120px;height:60px">获取角色+转写结果</button>
<br><br><br>
<button onclick=transTimeSpk() style="width:120px;height:60px">获取时间戳+角色+转写结果</button>
<br><br><br>
<button onclick=compareAudio() style="width:120px;height:60px">转写准确率比对结果（不同文件）</button>
<br><br><br>
<button onclick=compareMerge() style="width:120px;height:60px">转写准确率比对结果（同一文件）</button>
</div>
<script>
    function trans() {
        window.location.href = "http://localhost:8090/engineAudio";
    }
    function transSpk() {
        window.location.href = "http://localhost:8090/engineSpkAudio";
    }
    function transTimeSpk() {
        window.location.href = "http://localhost:8090/engineTimeSpkAudio";
    }
    function compareAudio() {
        window.location.href = "http://localhost:8090/engineAudioTagPath";
    }
    function compareMerge() {
        window.location.href = "http://localhost:8090/engineAudioOnePath";
    }
</script>

</body>
</html>
