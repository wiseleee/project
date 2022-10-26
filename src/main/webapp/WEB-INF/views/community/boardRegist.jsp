<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>📝작성하기</h1><br/>

<form action= "${pageContext.request.contextPath}/communitysvc/newBoard/register" method="post">
    <div>작성자<input type="text" name="nickname"></div>
    <div>제　목<input type="text" name="title"></div>내　용
    <div><textarea rows="15" cols="100" name="content"></textarea></div>
    <button type="submit">⭕작성하기</button>
</form>

</body>
</html>
