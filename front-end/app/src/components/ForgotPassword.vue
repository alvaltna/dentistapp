<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-xl-5 col-lg-6 col-md-8 col-sm-11">
        <h5>If You forgot Your password, enter Your username here and press 'Submit'.
          You will be sent a link which you can use to reset Your password.</h5>

        <form id="post-form" @submit.prevent="processForm">

          <div class="form-group text-left">
            <label for="username">Username:</label>
            <input id="username" class="form-control" type="text" name="username" placeholder="Username"
                   v-model="username" v-validate="{ required: true, min: 4, max: 128 }"
                   autocomplete="username">
            <div class="error" v-if="errors.has('username')">{{errors.first('username')}}</div>
          </div>

          <input class="btn btn-primary mb-3" type="submit" value="Submit">
        </form>
      </div>
    </div>
  </div>
</template>

<script>
    import apiRequests from './../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';
    import Swal from 'sweetalert2';

    export default {
        name: "ForgotPassword",
        data() {
            return {
                username: '',
            };
        },
        methods: {
            sendEmail() {
                Swal.fire({
                    title: 'Sending email...'
                });
                Swal.showLoading();

                apiRequests
                    .postRequestToApi('/api/forgotPassword', {
                        username: this.username
                    })
                    .then(() => {
                        Swal.close();
                        this.resetFields();
                        errorHandling.successMsg("Email sent!", 1000);
                    })
                    .catch((error) => {
                            Swal.close();
                            errorHandling.errorMsg(error.response.data.message, 1200)
                        }
                    );
            },
            resetFields() {
                this.username = '';
                this.$nextTick(() => this.$validator.reset());
            },
            processForm() {
                this.$validator.validate().then(valid => {
                    if (valid) {
                        this.sendEmail();
                    } else {
                        errorHandling.errorMsg("The form wasn't filled in properly!", 1000);
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>