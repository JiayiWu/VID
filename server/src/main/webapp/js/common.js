/**
 * Created by L.H.S on 2017/2/24.
 */

// var SERVER_IP = "http://115.28.210.167:8080/VID";
var SERVER_IP = '';

function showMenu() {
    $("#menu").slideDown(200);

    $("body").click(function () {
        if (event.target.className && event.target.className != "dialog_btn") {
            $("#menu").hide();
        }
    });
}

// xhr.readyState == 4 && xhr.status == 200
function sendXML(url, type, data) {

    var xhr;
    if (window.ActiveXObject) {
        //如果是IE浏览器
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    } else if (window.XMLHttpRequest) {
        //非IE浏览器
        xhr = new XMLHttpRequest();
    }

    xhr.open(type, url);
    xhr.responseType = "json";

    xhr.onload = function () {
        console.log(xhr.response);
    };

    xhr.onerror = function () {
        alert("error");
    };

    xhr.send(JSON.stringify(data));

    return xhr;
}

// 上传视频
function uploadVideo() {

    var ie = !-[1,];
    if (ie) {
        $("input:file").trigger('click').trigger('change');
    } else {
        $("input:file").trigger('click');
    }

    $("input:file").change(function () {
        var file = $("input:file")[0].files[0];

        var fileName = file.name;
        var fileSize = file.size;
        var filePath = $("input:file").val();

        var data = {
            name: fileName,
            size: fileSize,
            url: filePath
        };

        var xhr = sendXML(SERVER_IP + "/video/upload", "POST", data);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var data = xhr.response;

                alert(data.status)
            }
        };

    });

}