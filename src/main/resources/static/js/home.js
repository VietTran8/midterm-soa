var isSatisfy = {
    status: false,
    content: "Vui lòng điền đầy đủ thông tin"
}
var confirmPaymentBtn = $("#confirm-payment")
var closeOtpModalBtn = $("#btn-close-otp-modal")
var otpConfirmBtn = $("#otp-confirm")

closeOtpModalBtn.click(closeOtpModal())
otpConfirmBtn.click(() => {
    var otp = $("#otp")
    var transactionId = $("#transaction-id")
    var studentNumber = $("#studentNumber")

    validateOtp(otp.val(), parseInt(transactionId.val()), studentNumber.val())
})

// if(!sessionStorage.getItem("logged")){
//     var token = window.sessionStorage.getItem("token")
//     console.log(token)
//     $.ajax({
//         url: "http://localhost:8080/logout",
//         method: "post",
//         headers: {
//             "Authorization": token
//         },
//         success: (result) => {
//             localStorage.removeItem("token")
//             window.location.href="http://localhost:8080/login"
//         },
//         failure: (jqXHR) => {
//             var responseText = jqXHR.responseText
//             var responseObject = {}
//             if(JSON.parse(responseText)){
//                 responseObject = JSON.parse(responseText)
//                 alert(responseObject.message)
//             }else
//                 alert("Something went wrong, try again")
//         }
//     })
// }

confirmPaymentBtn.click(function() {
    var studentNumber = $("#studentNumber")
    var amount = $("#totalPay")
    
    if($("#term").is(":checked")){
        if(isSatisfy.status)
            createPayment(studentNumber.val(), parseFloat(amount.attr("placeholder").split(" ")[0].replace(/,/g, '')))
        else
            alert(isSatisfy.content)
    }else
        alert("Vui lòng đồng ý thỏa thuận và điều khoản")
   
})

$(".access-student-info").submit(function() {
    var form = $(this);
    event.preventDefault();
    event.stopPropagation();

    if (form[0].checkValidity() !== false) {
        fetchStudent()
    }
    form.addClass('was-validated');
})

$("#logout").click(function() {
    var token = window.sessionStorage.getItem("token")
    console.log(token)
    $.ajax({
        url: "http://localhost:8080/logout",
        method: "post",
        headers: {
            "Authorization": token
        },
        success: (result) => {
            localStorage.removeItem("token")
            window.location.href="http://localhost:8080/login"
        },
        failure: (jqXHR) => {
            var responseText = jqXHR.responseText
            var responseObject = {}
            if(JSON.parse(responseText)){
                responseObject = JSON.parse(responseText)
                alert(responseObject.message)
            }else
                alert("Something went wrong, try again")
        }
    })
})

var spinner = $(".spinner")

fetchUser()

function validateOtp(otp, transactionId, studentNumber){
    if(spinner.hasClass("d-none")){
        spinner.removeClass("d-none")
        spinner.addClass("d-flex")
    }
    $.ajax({
        url: "http://localhost:8080/api/otp/validate",
        method: "post",
        contentType: 'application/json',
        headers: {
            "Authorization": localStorage.getItem("token")
        },
        data: JSON.stringify({
            studentNumber: studentNumber,
            otp: otp,
            transactionId: transactionId
        }),
        success: (response) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")

            alert("Thanh toán thành công, vui lòng kiểm tra email của bạn")

            fetchUser()
            fetchStudent()
            closeOtpModal()
        },
        error: (jqXHR) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")

            alertError(jqXHR)
        }
    })
}

function closeOtpModal() {
    var enterOtpModal = $("#enter-otp-modal")
    if(enterOtpModal.hasClass("d-block")){
        enterOtpModal.removeClass("d-block")
        enterOtpModal.addClass("d-none")
    }
}

function createPayment(studentNumber){
    var enterOtpModal = $("#enter-otp-modal")
    var transactionIdInput = $("#transaction-id")
    var otpModalMessage = $("#otp-modal-message")

    if(spinner.hasClass("d-none")){
        spinner.removeClass("d-none")
        spinner.addClass("d-flex")
    }
    $.ajax({
        url: "http://localhost:8080/api/payment/create-payment",
        method: "post",
        contentType: 'application/json',
        headers: {
            "Authorization": localStorage.getItem("token")
        },
        data: JSON.stringify({
            studentNumber: studentNumber
        }),
        success: (response) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")

            otpModalMessage.text(response.message)

            transactionIdInput.val(response.data.id)
            if(enterOtpModal.hasClass("d-none")){
                enterOtpModal.removeClass("d-none")
                enterOtpModal.addClass("d-block")
            }
        },
        error: (jqXHR) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")

            alertError(jqXHR)
        }
    })
}

function alertError(jqXHR){
    var responseText = jqXHR.responseText
    console.log(responseText)
    var responseObject = {}
    if(JSON.parse(responseText)){
        responseObject = JSON.parse(responseText)
        alert(responseObject.message)
    }else
        alert("Something went wrong, try again")
}

function fetchUserTransaction(){

}

function fetchUser(){
    if(spinner.hasClass("d-none")){
        spinner.removeClass("d-none")
        spinner.addClass("d-flex")
    }

    var fullName = $("#fullName")
    var phoneNumber = $("#phoneNumber")
    var email = $("#email")
    var balance = $("#balance")
    var histBody = $(".hist-body")

    $.ajax({
        url: "/api/user",
        method: "get",
        headers: {
            "Authorization": localStorage.getItem("token")
        },
        success: (response) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")
            if(response.status){
                let user = response.data
                fullName.attr("placeholder", user.fullName)
                balance.attr("placeholder", formatNumber(user.balance) + " VNĐ")
                phoneNumber.attr("placeholder", user.phone)
                email.attr("placeholder", user.email)
                histBody.empty()

                var userTransactions = user.userTransactions
                userTransactions.forEach(transaction => {
                    var createdDate = transaction.createdAt.split(" ")
                    var newChild = $(`
                        <div class="card text-bg-light mb-3" style="max-width: 100%;">
                            <div class="card-header">
                                <i class="fa-solid fa-clock-rotate-left"></i>
                                <b id="paid-at" class="ms-1">${createdDate[0]} lúc ${createdDate[1]}</b>
                            </div>
                            <div class="card-body">
                                <h4 class="card-title" id="hist-amount">- ${formatNumber(transaction.amount)} VNĐ</h4>
                                <p id="hist-content" class="card-text">${transaction.content}</p>
                            </div>
                        </div>
                    `)

                    histBody.append(newChild)
                })
            }
        },
        error: (jqXHR) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")
            alertError(jqXHR)
        }
    })
}

function fetchStudent(){
    if(spinner.hasClass("d-none")){
        spinner.removeClass("d-none")
        spinner.addClass("d-flex")
    }

    var studentNumber = $("#studentNumber")
    var studentFullName = $("#studentFullName")
    var studentTuitionList = $(".student-tuition")
    var notFoundMessage = $("#not-found-message")
    var balance = $("#balance")
    var totalPay = $("#totalPay")

    $.ajax({
        url: "/api/student/" + studentNumber.val(),
        method: "get",
        headers: {
            "Authorization": localStorage.getItem("token")
        },
        success: (response) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")
            console.log(response)
            if(response.status){
                notFoundMessage.addClass("d-none")
                notFoundMessage.removeClass("d-block")
                let student = response.data

                studentFullName.attr("placeholder", student.fullName)

                studentTuition = student.tuitionList
                studentTuitionList.empty()

                studentTuition.forEach(element => {
                    var newChild = $(
                        `
                        <li class="list-group-item d-flex justify-content-between align-items-start list-group-item-action">
                            <div class="ms-2 me-auto">
                            <div class="fw-bold ${element.status ? "active" : ""}">${element.content}</div>
                            <span class="${element.status ? "active" : ""}">${formatNumber(element.amount)} VNĐ</span>
                            </div>
                        </li>
                        `
                    )
                    studentTuitionList.append(newChild)
                });

                totalPay.attr("placeholder", formatNumber(student.totalTuition) + " VNĐ")

                if(parseFloat(balance.attr("placeholder").replace(/,/g, '')) >= student.totalTuition){
                    isSatisfy.status = true
                    $("#balance-not-enough").removeClass("d-block")
                    $("#balance-not-enough").addClass("d-none")
                }else{
                    isSatisfy.status = false
                    isSatisfy.content = "Số dư không đủ!"
                    $("#balance-not-enough").addClass("d-block")
                    $("#balance-not-enough").removeClass("d-none")
                }
            }
        },
        error: (jqXHR) => {
            spinner.addClass("d-none")
            spinner.removeClass("d-flex")
            notFoundMessage.removeClass("d-none")
            notFoundMessage.addClass("d-block")
            clearStudentInfo()
            $("#balance-not-enough").removeClass("d-block")
            $("#balance-not-enough").addClass("d-none")
        }
    })
}

function clearStudentInfo(){
    var studentFullName = $("#studentFullName")
    var studentTuitionList = $(".student-tuition")
    var totalPay = $("#totalPay")

    studentFullName.attr("placeholder", "")
    totalPay.attr("placeholder", "")
    studentTuitionList.empty()

    isSatisfy.status = false
    isSatisfy.content = "Vui lòng điền đầy đủ thông tin"

}

function formatNumber(input) {
    const number = parseFloat(input);
    if (isNaN(number)) {
      return 'Invalid input';
    }
  
    return formattedNumber = number.toLocaleString('en-US', { minimumFractionDigits: 0 });
}  