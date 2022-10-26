<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Title</title>
    <script src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>
    <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
    <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <script src="./jquery-3.4.1.min.js"></script>
    <link rel="stylesheet" href="./bootstrapt/css/bootstrap.min.css" />
    <link rel="stylesheet" href="./bootstrapt/css/bootstrap.css" />
    <script src="./bootstrapt/js/bootstrap.min.js"></script>
    <style>

        section .section-title {
            text-align: center;
            color: #007b5e;
            text-transform: uppercase;
        }

        #tabs {
            background: #333333;
            color: #eee;
            background-color: rgba(051, 051, 051, 0.8);
        }

        #tabs h6.section-title {
            color: #eee;
        }

        #tabs .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active {
            color: #f3f3f3;
            background-color: transparent;
            border-color: transparent transparent #f3f3f3;
            border-bottom: 4px solid !important;
            font-size: 20px;
            font-weight: bold;
        }

        #tabs .nav-tabs .nav-link {
            border: 1px solid transparent;
            border-top-left-radius: .25rem;
            border-top-right-radius: .25rem;
            color: #eee;
            font-size: 20px;
        }

        #my_modal {
            display: none;
            width: 300px;
            padding: 20px 60px;
            background-color: #fefefe;
            border: 1px solid #888;
            border-radius: 3px;
        }

        #my_modal .modal_close_btn {
            position: absolute;
            top: 10px;
            right: 10px;
        }
        .btn{
            background: #ffdf7e;
        }

        .btn2{
            background: #F15F5F;
            border: #F15F5F;
            color: beige;
            font-size: 20px;
        }
        .modal-body{
            background: lightgrey;
            border: double;
            left: 10%;
            width: 400px;
            height: 100px;

        }
        .modal-title{
            border: double;
            top: 0;
            left: 0;
        }

    </style>
    <script>
        const myGrid = document.querySelector("#myGrid");
        const deleteBtn = document.querySelector("#deleteButton");
        //   let removeData = gridOptions.getSelectedRowData();
        var boardlist = [];
        var boardData = [];
        var updateData = {};
       // 리터럴 표기법이 다름. 객체는 = {}, 배열은 = []

        $(document).ready(function () {

            $("#modifyButton").click(function (){
                modifyboard();
            });

            $.ajax({
                url: "${pageContext.request.contextPath}/communitysvc/newBoard/list",
                dataType: "json",
                success: function (data) {
                   // console.log(data);
                    console.log(data.boardlist[0].id);
                    boardlist = data.boardlist;
                    console.log("boardlist?"+boardlist);

                    let boardColumn = [
                        {headerName: "번호", field: "id",
                            width: 50, cellStyle: {'textAlign': 'center'},
                          //  headerCheckboxSelection: true,
                            checkboxSelection: true},
                        {headerName: "이름", field: "nickname", width: 50, cellStyle: {'textAlign': 'center'} },
                        {headerName: "제목", field: "title", width: 50, cellStyle: {'textAlign': 'center'}},
                        {headerName: "작성일", field: "askdate", width: 50, cellStyle: {'textAlign': 'center'}}
                    ];

                     gridOptions = {
                        defaultColDef: {
                            flex: 1,
                            minWidth: 100,
                            resizable: true,
                            editable: false
                        },
                        columnDefs: boardColumn, // def 속성
                        rowData: boardlist,
                        rowSelection: 'single', // 하나만 선택가능 (반대는 multiple)
                        enableColResize: true,
                        enableSorting: true,
                        enableFilter: true,
                        enableRangeSelection: true,
                        suppressRowClickSelection: false,
                        animateRows: true,
                        deletable: false,
                        suppressHorizontalScroll: true,
                        getRowStyle: function (param) {
                            if (param.node.rowPinned) {
                                return {
                                    'font-weight': 'bold',
                                    'background' : '#dddddd'
                                };
                            }
                            return {
                                'text-align': 'center'
                            };
                        },
                        getRowHeight: function (param) {
                            if (param.node.rowPinned) {
                                return 30;
                            }
                            return 24;
                        },
                        // GRID READY 이벤트, 사이즈 자동조정,onload 이벤트와 유사 ready 이후 필요한 이벤트 삽입한다.
                        onGridReady: function (event) {
                            event.api.sizeColumnsToFit();
                        },
                        // 창 크기 변경 되었을 때 이벤트
                        onGridSizeChanged: function (event) {
                            event.api.sizeColumnsToFit();
                        },
                        onCellClicked : function (event){ //셀클릭시 모달 띄우기,
                            console.log("event?"+event);
/*                          console.log(this.title);
                            let a =this.title;
                            console.log(a);
                            $('#modalTitle').val(this.title);
                            console.log($('#modalTitle').val());
                            $('#contents').val(this.content);*/
                            $('#detailModal').modal('show');
                            const id = event.data.id;
                            newBoardnum(id);
                            // $('#detailModal').modal('hide');
                        },  //Row Click 하는 경우 발생하는 이벤트, 모달 팝업을 띄우거나 다른 페이지로 이동하거나 클릭시 무언가를 처리해야 하는 경우
                        onRowSelected: function (event) { // checkbox누르면 값 얻어옴
                            console.log(event);
                            const id = event.data.id;
                            console.log("?"+id);
                            return id;
                        },
                  /*      getSelectedRowData() {
                            let selectedNodes = this.api.getSelectedNodes();
                            let selectedData = selectedNodes.map(node => node.data);
                            console.log(selectedData+"여기");
                            return selectedData;
                        }*/
                    };
                    new agGrid.Grid(document.querySelector('#myGrid'), gridOptions);
                } //그리드를 '#myGrid' 이 공간에 띄움
            });


            function newBoardnum(id){
                $.ajax({
                    url    : "${pageContext.request.contextPath}/communitysvc/newBoard/detail",
                    data : { "id" : id },
                    dataType: "json",
                    success: function(data){
                        console.log(data['boardDetail']);
                        let detail = data['boardDetail'];
                        let id=detail.id;
                        console.log(detail.title);
                        let title=detail.title;
                        let content=detail.content;
                        $('#modalid').val(id);
                        $('#modalTitle').val(title);
                        $('#contents').val(content);
                           // let details = JSON.stringify(detail);
                        //titles.value = detail.data.title;
                        //contents.value = detail.data.content;
                    },

                });
            }
/*            deleteBtn.addEventListener("click", () => {
                let a = gridOptions.api.getSelectedRowData();
                console.log(a+"요기2");

            });*/

            $('#deleteButton').click(function() {
                var selectedRowData = gridOptions.api.getSelectedRows();
                var id = JSON.stringify(selectedRowData[0].id); //삭제할 목록들이 담긴 배열
                console.log(id);
                removeRow(id);
                //    $('#detailModal').is("checked");


        function removeRow(id) {
            console.log("확인"+id);
            $.ajax({
                url    : "${pageContext.request.contextPath}/communitysvc/newBoard/remove",
                data : { "id" : id },
                dataType : "json",
                type : "DELETE",
                success: function(data){
                    console.log(data);
                    if(data.errorCode < 0){
                        alert("정상적으로 삭제되지 않았습니다");
                    } else {
                        alert(data.errorMsg);
                    }
                    location.reload();
                }
                 /*   let selectedData = gridOptions.api.getSelectedRows();
                    gridOptions.api.updateRowData({remove: selectedData});
                   /!* $("#deleteButton").is("checked")*!/
                },*/
                /*error: function(xhr, status, error) {
                    alert(error+"오류입니다");
                }*/
            });
        }
            });
            function saveinfo(){
                updateData.id=$("#modalid").val();
                updateData.title=$("#modalTitle").val();
                updateData.content=$("#contents").val();
                console.log(updateData);
            } //updateData객체에 id,title,content 컬럼에 #modalid, #modalTitle, #contents 의 값을 가져오거나 설정함

            function modifyboard(){
                saveinfo();
               // console.log(saveinfo()); -> 이거 값나오게 하려면 saveinfo()에 return값을 줘야함
                console.log(JSON.stringify(updateData));
                //json객체를 문자열로 바꿔서 값이 나오는지 찍어봄
                $.ajax({
                    type: "PUT",
                    url    : "${pageContext.request.contextPath}/communitysvc/newBoard/modify",
                    data : { "updateData" : JSON.stringify(updateData) }, //데이터값 보낼때도 문자열로 바꿔서 보냄
                    dataType: "json",
                    success: function(data){
                        if(data.errorCode < 0){
                            alert("정상적으로 수정되지 않았습니다");
                        } else {
                            alert(data.errorMsg);
                        }
                        location.reload();
                      //  {"href":"${pageContext.request.contextPath}/community/board/view"}.href;
                    }

                });
            }
    });

    </script>
</head>
<body>
<article class="newboard">
    <div class="newboard_Title">
        <h5>📋 게시판</h5>
        <form action="/community/boardRegist/view">
        <div class="menuButton" style=" text-align: right;">
            <button id="writeButton">글쓰기</button>
            <input type="button"  id="deleteButton" value="삭제" />
        </div>
        </form>
    </div>
</article>
<article class="myGrid">
    <div align="center">
        <div id="myGrid" class="ag-theme-balham" style="height:70vh; width:auto; text-align: center;"></div>
    </div>
</article>

<div class="modal fade" id="detailModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="false">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <input type='hidden' id='modalid'  readonly /><br>
                <label for='modalTitle'  >🖤 제목 :　</label>
                <input type='text' id='modalTitle' class="modal-title" autocomplete="off" readonly /><br>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <button type="button" class="close" aria-label="Close" data-dismiss="modal" ><span aria-hidden="true">❌</span></button>
                </button>
            </div>
            <label for='contents'  >　🖤 내용 :</label>
            <input type='text' id='contents' class="modal-body" autocomplete="off" /><br>
            <div class="modal-footer">
                <%--<a class="btn" id="modalY" href="/community/boardupdate/view">수정</a>--%>
                <%--<input type="button" value="수정" class="btn2" id="modifyButton" >--%>
                <button class="btn2" type="button" id="modifyButton">수정</button>
                <button class="btn" type="button" data-dismiss="modal">목록</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
