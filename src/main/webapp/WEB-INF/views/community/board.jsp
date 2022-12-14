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
       // ????????? ???????????? ??????. ????????? = {}, ????????? = []

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
                        {headerName: "??????", field: "id",
                            width: 50, cellStyle: {'textAlign': 'center'},
                          //  headerCheckboxSelection: true,
                            checkboxSelection: true},
                        {headerName: "??????", field: "nickname", width: 50, cellStyle: {'textAlign': 'center'} },
                        {headerName: "??????", field: "title", width: 50, cellStyle: {'textAlign': 'center'}},
                        {headerName: "?????????", field: "askdate", width: 50, cellStyle: {'textAlign': 'center'}}
                    ];

                     gridOptions = {
                        defaultColDef: {
                            flex: 1,
                            minWidth: 100,
                            resizable: true,
                            editable: false
                        },
                        columnDefs: boardColumn, // def ??????
                        rowData: boardlist,
                        rowSelection: 'single', // ????????? ???????????? (????????? multiple)
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
                        // GRID READY ?????????, ????????? ????????????,onload ???????????? ?????? ready ?????? ????????? ????????? ????????????.
                        onGridReady: function (event) {
                            event.api.sizeColumnsToFit();
                        },
                        // ??? ?????? ?????? ????????? ??? ?????????
                        onGridSizeChanged: function (event) {
                            event.api.sizeColumnsToFit();
                        },
                        onCellClicked : function (event){ //???????????? ?????? ?????????,
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
                        },  //Row Click ?????? ?????? ???????????? ?????????, ?????? ????????? ???????????? ?????? ???????????? ??????????????? ????????? ???????????? ???????????? ?????? ??????
                        onRowSelected: function (event) { // checkbox????????? ??? ?????????
                            console.log(event);
                            const id = event.data.id;
                            console.log("?"+id);
                            return id;
                        },
                  /*      getSelectedRowData() {
                            let selectedNodes = this.api.getSelectedNodes();
                            let selectedData = selectedNodes.map(node => node.data);
                            console.log(selectedData+"??????");
                            return selectedData;
                        }*/
                    };
                    new agGrid.Grid(document.querySelector('#myGrid'), gridOptions);
                } //???????????? '#myGrid' ??? ????????? ??????
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
                console.log(a+"??????2");

            });*/

            $('#deleteButton').click(function() {
                var selectedRowData = gridOptions.api.getSelectedRows();
                var id = JSON.stringify(selectedRowData[0].id); //????????? ???????????? ?????? ??????
                console.log(id);
                removeRow(id);
                //    $('#detailModal').is("checked");


        function removeRow(id) {
            console.log("??????"+id);
            $.ajax({
                url    : "${pageContext.request.contextPath}/communitysvc/newBoard/remove",
                data : { "id" : id },
                dataType : "json",
                type : "DELETE",
                success: function(data){
                    console.log(data);
                    if(data.errorCode < 0){
                        alert("??????????????? ???????????? ???????????????");
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
                    alert(error+"???????????????");
                }*/
            });
        }
            });
            function saveinfo(){
                updateData.id=$("#modalid").val();
                updateData.title=$("#modalTitle").val();
                updateData.content=$("#contents").val();
                console.log(updateData);
            } //updateData????????? id,title,content ????????? #modalid, #modalTitle, #contents ??? ?????? ??????????????? ?????????

            function modifyboard(){
                saveinfo();
               // console.log(saveinfo()); -> ?????? ???????????? ????????? saveinfo()??? return?????? ?????????
                console.log(JSON.stringify(updateData));
                //json????????? ???????????? ????????? ?????? ???????????? ?????????
                $.ajax({
                    type: "PUT",
                    url    : "${pageContext.request.contextPath}/communitysvc/newBoard/modify",
                    data : { "updateData" : JSON.stringify(updateData) }, //???????????? ???????????? ???????????? ????????? ??????
                    dataType: "json",
                    success: function(data){
                        if(data.errorCode < 0){
                            alert("??????????????? ???????????? ???????????????");
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
        <h5>???? ?????????</h5>
        <form action="/community/boardRegist/view">
        <div class="menuButton" style=" text-align: right;">
            <button id="writeButton">?????????</button>
            <input type="button"  id="deleteButton" value="??????" />
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
                <label for='modalTitle'  >???? ?????? :???</label>
                <input type='text' id='modalTitle' class="modal-title" autocomplete="off" readonly /><br>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <button type="button" class="close" aria-label="Close" data-dismiss="modal" ><span aria-hidden="true">???</span></button>
                </button>
            </div>
            <label for='contents'  >??????? ?????? :</label>
            <input type='text' id='contents' class="modal-body" autocomplete="off" /><br>
            <div class="modal-footer">
                <%--<a class="btn" id="modalY" href="/community/boardupdate/view">??????</a>--%>
                <%--<input type="button" value="??????" class="btn2" id="modifyButton" >--%>
                <button class="btn2" type="button" id="modifyButton">??????</button>
                <button class="btn" type="button" data-dismiss="modal">??????</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
