<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>πμμ±νκΈ°</h1><br/>

<form action= "${pageContext.request.contextPath}/communitysvc/newBoard/register" method="post">
    <div>μμ±μ<input type="text" name="nickname"></div>
    <div>μ γλͺ©<input type="text" name="title"></div>λ΄γμ©
    <div><textarea rows="15" cols="100" name="content"></textarea></div>
    <button type="submit">β­μμ±νκΈ°</button>
</form>

</body>
</html>
