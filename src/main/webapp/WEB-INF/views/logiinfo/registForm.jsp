<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<style>
	form{
		margin: 40px 450px;
	}
	td{
		color:black;
	}
	h3{
		color:black;
	}
	fieldset{
		margin:0;
		border: 1px solid white;
	}
		.card1 {
  		background-color: #FFFFFF !important;
  		border-radius: 10px;
	}
</style>
<script src="https://code.jquery.com/jquery-3.5.0.js"></script>
<script src="https://malsup.github.io/jquery.form.js"></script> 
<script>
   $(document).ready(function() {
      var todayTime = new Date();         
      var rrrr = todayTime.getFullYear();
      var mm = todayTime.getMonth()+1;
      var dd = todayTime.getDate();
      var today = rrrr+"-"+addZeros(mm,2)+"-"+addZeros(dd,2);
      console.log(today);
      $("#date").val(today);
      
      $("#registBoard").click(function(){
         $("#regist_board").submit();
      });
      
      $("#registboard").ajaxForm({
         dataType: "json",
         success: function(data){ 
            alert(data.errorMsg);
           // location.href = "/logiinfo/boardList/view";
            location.reload();
         }
      });   
   });

   /* 날짜 자리수 맞춰주는 함수 */
   function addZeros(num, digit) {           
      var zero = '';
       num = num.toString();
       if (num.length < digit) {
          for (i = 0; i < digit - num.length; i++) {
           zero += '0';
          }
       }
       return zero + num;
   }
</script>
</head>
<body>
   <h3><small>&nbsp;</small></h3>
   <h3><small>&nbsp;</small></h3>
   <center>
      <form id="regist_board" action="/compinfo/board?method=registerBoard" method="post" enctype="multipart/form-data">
         <div class="container wow fadeInDown card1">
         <h3>글등록</h3>
         <table class="c1">            
            <tr>   
               <td>이름
               <input type="text" name="name" value="${sessionScope.empName}${sessionScope.positionName}">
            </tr>
            <tr>   
               <td>제목
               <input type="text" name="title" maxlength="20">
               </td>
            </tr>
            <tr>   
               <td>작성일자
               <input type="text" name="reg_date" style="width:137px;" id="date" value="${today}">
               </td>
            </tr>
            <tr>
               <td colspan="4">
                  <textarea cols="25" rows="4" name="content"></textarea>
               </td>
            </tr>   
            <tr>
               <td colspan="4">
                  <input type="file" name="uploadFile" style="margin-right:-80px;">
               </td>
            </tr>
            <tr>   
               <td colspan="4">
                  <input type="button" value="등록" id="registBoard">
                  <input type="reset" value="취소">
                  <a href="/logiinfo/boardList/view" style=color:black>목록으로</a>
               </td>
            </tr>
         </table>
         <input type="hidden" name="board_seq" value="${param.board_seq}">
         </div>
      </form>
   </center>
</body>
</html>