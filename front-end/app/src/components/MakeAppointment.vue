<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-xl-5 col-lg-6 col-md-8 col-sm-11">

        <h3>Make an appointment</h3>

        <form id="post-form" @submit.prevent="processForm">

          <div class="form-group text-left">
            <label for="dentistName">Dentist's name:</label>
            <select class="form-control" id="dentistName" name="dentistName" v-model="dentistName"
                    v-validate="{ required: true }">dentistappDB@localhost
              <option value="Mart Tamm">Mart Tamm</option>
              <option value="Maire Kuusk">Maire Kuusk</option>
              <option value="Pille Paju">Pille Paju</option>
              <option value="Tiit Haab">Tiit Haab</option>
              <option value="Milvi Pihlakas">Milvi Pihlakas</option>
              <option value="Priit Vaher" selected="selected">Priit Vaher</option>
            </select>
            <div class="error" v-if="errors.has('dentistName')">{{errors.first('dentistName')}}</div>
          </div>
          <div class="form-group text-left">
            <label for="visitTime">Appointment date and time:</label>
            <VueCtkDateTimePicker id="visitTime" label="Select date and time" name="visitTime"
                                  format='YYYY-MM-DD HH'
                                  v-validate="{required: true}"
                                  :min-date="minDate"
                                  v-model="visitTime"
                                  :minute-interval="60"
                                  :disabled-hours="disabledHours">

            </VueCtkDateTimePicker>
            <div class="error" v-if="errors.has('visitTime')">{{errors.first('visitTime')}}</div>
          </div>
          <div class="form-group text-left">
            <label for="procedure">Procedure:</label>
            <input id="procedure" class="form-control" type="text" name="procedure"
                   placeholder="Procedure" v-model="procedure" v-validate="{ required: true, min: 5 }">
            <div class="error" v-if="errors.has('procedure')">{{errors.first('procedure')}}</div>
          </div>
          <div class="form-group text-left">
            <label for="description">Description:</label>
            <textarea class="form-control" id="description" rows="3"
                      name="description" placeholder="Description"
                      v-model="description" v-validate="{ required: true, min: 5 }">
            </textarea>
            <div class="error" v-if="errors.has('description')">{{errors.first('description')}}</div>
          </div>
          <input class="btn btn-lg btn-primary mb-3" type="submit" value="Submit">
        </form>
      </div>
    </div>
  </div>
</template>


<script>
    import apiRequests from './../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';
    import VueCtkDateTimePicker from 'vue-ctk-date-time-picker';
    import 'vue-ctk-date-time-picker/dist/vue-ctk-date-time-picker.css';
    import Datepicker from 'vuejs-datepicker';


    export default {
        name: 'makeAppointment',
        components: {
          VueCtkDateTimePicker,


      },
        data() {
            return {

                dentistName: '',
                visitTime: '',
                procedure: '',
                description: '',
                dateTime: '',
                disabledHours: ['00','01','02','03','04','05','06','07','19','20','21','22','23'],
                minDate:'',
            };
        },
        methods: {

            resetFields() {
                this.dentistName = '';
                this.visitTime = '';
                this.procedure = '';
                this.description = '';
                this.$nextTick(() => this.$validator.reset())
            },
            postFormData() {
                apiRequests
                    .postRequestToApiWithAuthorization('/api/add/visit', {
                        dentistName: this.dentistName,
                        visitTime: this.visitTime,
                        procedure: this.procedure,
                        description: this.description,
                    })
                    .then(() => {
                        errorHandling.successMsg("Appointment successfully made!", 1200);
                        this.resetFields();
                    })
                    .catch((error) => {
                        if (error.response.status === 401 || error.response.status === 403) {
                            errorHandling.errorMsgWithButton("You need to be logged in to make an appointment!");
                        }
                        else if(error.response.status === 400) {
                          errorHandling.errorMsgWithButton("This appointment time is already taken. " +
                                  "Please try a different datetime.")
                        }
                        else {
                            errorHandling.errorMsgWithButton("Sorry, " +
                                "there was a problem and could not make an appointment!");
                        }
                    });
            },
            processForm() {
                this.$validator.validate().then(valid => {
                    if (valid) {
                            this.postFormData();
                    } else {
                        errorHandling.errorMsg("The form wasn't filled in properly!", 1000);
                    }
                });
            }
        },
      mounted() {
        let moment = require('moment'); // require
        this.minDate = moment().format("YYYY-MM-DD HH");
      },
    };
</script>


