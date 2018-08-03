if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}


$(document).ready(function(){

    $("#sum").click(function(){
        $.ajax({
            url: "/save1/"+$("#topic").val(),
           // data: $("#topic").val(),
            success: function(result){
                console.log( result);
                //  $("#data").html(result);
            }
        });
    });

})
