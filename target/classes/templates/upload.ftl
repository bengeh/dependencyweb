<!DOCTYPE HTML>
<html ng-app="fileManagerApp" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />




    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>



    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

   
    <link href="css/main.css" rel="stylesheet"/>
    
    




    <div id="spinner" class="spinner" style="display:none;">
        <img id="img-spinner" src="../../css/spinner.gif" alt="Loading"/>
    </div>

    <script type="text/javascript">
        $(document).ready(function(){
        $("#spinner").bind("ajaxSend", function() {
            $(this).show();
        }).bind("ajaxStop", function() {
            $(this).hide();
        }).bind("ajaxError", function() {
            $(this).hide();
        });

         });
    </script>

    <script type="text/javascript">
         $(function(){
            $("#navBar").load("/navBar");
         });
     </script>

</head>

<body>


<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>               
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/1.0.3/angular-ui-router.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/localforage/1.5.0/localforage.min.js"></script>        
<script src="https://cdnjs.cloudflare.com/ajax/libs/ngStorage/0.3.10/ngStorage.min.js"></script>           
<script src="js/app.js"></script>                                                                          
<script src="js/FileService.js"></script>                                                                  
<script src="js/FileController.js"></script>                                                               

    <div class="container-fluid" id="navBar"></div>
    <br/>



        <div id="id_confrmdiv">Have you scanned this file before?
            <button onclick="unhide(this, 'scanned'); hide(this,'upload');">Yes</button>
            <button onclick="unhide(this, 'upload'); hide(this,'scanned')">No</button>
        </div>

        <br/>

        <div class="hidden" id="scanned">

            <form method="POST" action="/scanned">
                Please key in the name of the file: <input type="text" name="fileName" placeholder="file name"/>
                <input type="submit" value="Submit"/>
            </form>
            <div class="panel panel-default">
                <div class="panel-heading"><span class="lead">List of Files </span></div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class = "table table-hover">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>File Name</th>
                                <th>Time</th>
                            </tr>
                            </thead>

                            <tbody>
                                <tr ng-repeat="u in ctrl.getAllFiles()">
                                    Things inside the u controller: {{u}}
                                    <td>{{u.id}}</td>
                                    <td>{{u.fileName}}</td>
                                    <td>{{u.time}}</td>
                                </tr>
                            </tbody>
                            
                        </table>
                    </div>
                </div>
            </div>
            
        </div>

    








    <!--<div class="hidden" className="container" id="scanned">
        <table className="table table-striped">
            <thead>
            <tr>
                <th>File Name</th>
                <th>Report Results</th>
                <th>Time </th>
            </tr>
            </thead>
            <tbody>{rows}</tbody>
        </table>
    </div>-->



    <div class="hidden" id="upload">
        <form method="POST" action="/upload" enctype="multipart/form-data">
            File to upload: <input type="file" name="file" /><br/><br/>
            <input type="submit" value="Submit" id="button-upload" />
        </form>
    </div>

    <script type="text/javascript">
        $(document).ready(function(){
            $('#button-upload').click(function() {
                $('#spinner').show();
            });
        });
    </script>

    <script type="text/javascript">

        function unhide(clickedButton, divID) {
            var item = document.getElementById(divID);
            if (item) {
                if(item.className=='hidden'){
                    item.className = 'unhidden' ;
                    clickedButton.value = 'hide';
                }
        }}

        function hide(clickedButton, divID) {
            var item = document.getElementById(divID);
            if (item) {
                if(item.className=='unhidden'){
                    item.className = 'hidden' ;
                    clickedButton.value = 'unhide';
                }
        }}


    </script>


</body>
</html>