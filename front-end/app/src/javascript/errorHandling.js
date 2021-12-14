import Swal from 'sweetalert2';

export default {
    errorMsg(msg, time) {
        Swal.fire({
            position: 'center',
            type: 'error',
            title: msg,
            showConfirmButton: false,
            timer: time
        })
    },
    successMsg(msg, time) {
        Swal.fire({
            position: 'center',
            type: 'success',
            title: msg,
            showConfirmButton: false,
            timer: time
        })
    },
    successMsgWithButton(msg) {
        Swal.fire({
            type: 'success',
            title: msg
        })
    },
    errorMsgWithButton(msg) {
        Swal.fire({
            type: 'error',
            title: msg
        })
    }
}
