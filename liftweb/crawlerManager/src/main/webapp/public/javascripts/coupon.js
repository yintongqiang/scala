
    function getContent(url){
        alert(1)
        $.ajax ({
            'type':     'GET',
            'url':      '/task',

            'success':  function (msg) {
                alert("Sucess!"+msg)
                $("#ri").html(msg)

            },
            'error': function(error){
               alert("failed!")
            }
        });
    }
