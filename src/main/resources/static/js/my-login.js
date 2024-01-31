'use strict';
$(function() {
	console.log(localStorage.getItem("token"))
	var spinner = $(".spinner")
	$("input[type='password'][data-eye]").each(function(i) {
		var $this = $(this),
			id = 'eye-password-' + i,
			el = $('#' + id);

		$this.wrap($("<div/>", {
			style: 'position:relative',
			id: id
		}));

		$this.css({
			paddingRight: 60
		});
		$this.after($("<div/>", {
			html: 'Hiện',
			class: 'btn btn-primary btn-sm',
			id: 'passeye-toggle-'+i,
		}).css({
				position: 'absolute',
				right: 10,
				top: ($this.outerHeight() / 2) - 12,
				padding: '2px 7px',
				fontSize: 12,
				cursor: 'pointer',
		}));

		$this.after($("<input/>", {
			type: 'hidden',
			id: 'passeye-' + i
		}));

		var invalid_feedback = $this.parent().parent().find('.invalid-feedback');

		if(invalid_feedback.length) {
			$this.after(invalid_feedback.clone());
		}

		$this.on("keyup paste", function() {
			$("#passeye-"+i).val($(this).val());
		});
		$("#passeye-toggle-"+i).on("click", function() {
			if($this.hasClass("show")) {
				$this.attr('type', 'password');
				$this.removeClass("show");
				$(this).removeClass("btn-outline-primary");
			}else{
				$this.attr('type', 'text');
				$this.val($("#passeye-"+i).val());
				$this.addClass("show");
				$(this).addClass("btn-outline-primary");
			}
		});
	});

	$(".my-login-validation").submit(function() {
		var form = $(this);
		var username = $("#username").val()
		var password = $("#password").val()

		event.preventDefault();
        event.stopPropagation();
        if (form[0].checkValidity() !== false) {
          doAuth(username, password)
        }
		form.addClass('was-validated');
	});

	var doAuth = (username, password) => {
		if(spinner.hasClass("d-none")){
			spinner.removeClass("d-none")
			spinner.addClass("d-flex")
		}
		$.ajax({
			url: "http://localhost:8080/api/auth/login",
			method: "post",
			contentType: 'application/json',
			async: true,
			data: JSON.stringify({
				"username": username,
				"password": password
			}),
			success: (result) => {
				spinner.addClass("d-none")
				spinner.removeClass("d-flex")
				if(result.status){
					// sessionStorage.setItem("logged", true)
					localStorage.setItem("token", result.data.tokenType + " " + result.data.token)
					window.location.href = "http://localhost:8080/home"
				}else{
					alert(result.message)
				}
			},
			error: (jqXHR) => {
				spinner.addClass("d-none")
				spinner.removeClass("d-flex")
			    var responseText = jqXHR.responseText
				var responseObject = {}
				if(JSON.parse(responseText)){
					responseObject = JSON.parse(responseText)
					alert(responseObject.message)
				}else
					alert("Something went wrong, try again")
			}
		})
	}
});