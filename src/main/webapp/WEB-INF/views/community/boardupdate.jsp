<%@ page import="kr.co.seoulit.logistics.communitysvc.service.NewBoardService" %>
<%@ page import="kr.co.seoulit.logistics.communitysvc.to.NewBoardTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>πμμ νκΈ°</h1><br/>
<script>
    $(document).ready(function () {
        console.log("μ½μ");
        //$('').val();
        //const id = event.data.id;
        //console.log("?"+id);
        newBoardnum();
        function newBoardnum(){
            console.log("?");
        $.ajax({
            url: "${pageContext.request.contextPath}/communitysvc/newBoard/detail",
            dataType: "json",
            data : { "id" : id },
            success: function (data) {
                console.log(data);
               // console.log(data.boardlist[0].id);
               // boardlist = data.boardlist;
               // console.log("boardlist?" + boardlist);
                let nickname=detail.nickname;
                let title=detail.title;
                let content=detail.content;
                $('#nick1').val(nickname);
                $('#title1').val(title);
                $('#content1').val(content);
            }
        });
        }
    });
</script>
<form action= "${pageContext.request.contextPath}/communitysvc/newBoard/modify" method="put">
    <div>μμ±μ<input type="text" name="nickname" id="nick1" value="${newBoardTO.nickname}" style="text-align: center" readonly ></div>
    <div>μ γλͺ©<input type="text" name="title" id="title1" readonly ></div>λ΄γμ©
    <div><textarea rows="15" cols="100" name="content" id="content1" ></textarea></div>
    <button type="submit">βμμ νκΈ°</button>
</form>

</body>
</html>
