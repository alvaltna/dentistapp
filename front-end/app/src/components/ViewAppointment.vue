<template>
    <div class="container-fluid pb-5">
        <div class="row justify-content-center">
            <div class="col-xl-7 col-lg-8 col-md-9 col-sm-11">
                <div class="post-item text-left p-3 mb-3">
                    <div class="text-left">
                        <h3>Dentist name: {{response.dentistName}}</h3>
                        <p>Visit time: {{ formatDateTime}}</p>
                        <p>Procedure: {{response.procedure}}</p>
                        <p>Description: {{response.description}}</p>
                        <p class="mt-2 mb-1">
                            <small>
                                Made by: {{response.madeBy}}
                            </small>
                        </p>
                    </div>
                    <div class="form-group mx-4 mb-2" v-if="editingPost === response.id">
                        <select class="form-control mb-2" id="newDentistName" name="newDentistName"
                                v-model="newDentistName" v-validate="{ required: true }">
                            <option value="Mart Tamm">Mart Tamm</option>
                            <option value="Maire Kuusk">Maire Kuusk</option>
                            <option value="Pille Paju">Pille Paju</option>
                            <option value="Tiit Haab">Tiit Haab</option>
                            <option value="Milvi Pihlakas">Milvi Pihlakas</option>
                            <option value="Priit Vaher" selected="selected">Priit Vaher</option>
                        </select>
                        <div class="error" v-if="errors.has('newDentistName')">{{errors.first('newDentistName')}}</div>
                        <div class="form-group text-left">
                            <VueCtkDateTimePicker id="DatePicker" label="Select date and time" name="visitTime"
                                                  format='YYYY-MM-DD HH'
                                                  v-validate="{ required: true}"
                                                  :min-date="minDate"
                                                  v-model="newVisitTime"
                                                  :minute-interval="60"
                                                  :disabled-hours="disabledHours">
                            </VueCtkDateTimePicker>
                        </div>
                        <div class="error" v-if="errors.has('newVisitTime')">{{errors.first('newVisitTime')}}</div>
                        <input class="form-control mb-2" name="newProcedure"
                               v-model="newProcedure" v-validate="{ required: true, min: 3, max: 128 }">
                        <div class="error" v-if="errors.has('newProcedure')">{{errors.first('newProcedure')}}</div>
                        <textarea class="form-control mb-2" rows="3" name="newDescription"
                                  v-model="newDescription" v-validate="{ required: true, min: 5 }"></textarea>
                        <div class="error" v-if="errors.has('newDescription')">{{errors.first('newDescription')}}</div>
                        <button class="btn btn-primary m-1" @click="saveEditAppointment(response.id)">Save
                        </button>
                        <button class="btn btn-secondary m-1" @click="disableEditingAppointment">Cancel</button>
                    </div>
                    <button v-if="response.canDelete" class="btn btn-danger m-1" type="submit"
                            @click="deleteAppointment(response.id)">Cancel appointment
                    </button>
                    <button v-if="response.canDelete" class="btn btn-warning m-1" type="submit"
                            @click="enableEditingAppointment()">Edit appointment
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import apiRequests from '../javascript/apiRequests.js';
    import errorHandling from './../javascript/errorHandling.js';
    import VueCtkDateTimePicker from 'vue-ctk-date-time-picker';
    import 'vue-ctk-date-time-picker/dist/vue-ctk-date-time-picker.css';


    export default {
        name: 'viewAppointment',
        components: {
            VueCtkDateTimePicker
        },
        data() {

            return {
                response: [],
                newDentistName: null,
                newVisitTime: null,
                newProcedure: null,
                newDescription: null,
                editingPost: false,
                dateTime: "",
                disabledHours: ['00','01','02','03','04','05','06','07','19','20','21','22','23'],
                minDate:'',

            };
        },
        computed: {
            formatDateTime: function () {
                //this.response.visitTime.format("YYYY-MM-DDTHH:MM:SS");
                return this.response.visitTime + ":00"
            },
        },
        methods: {

            loadAppointment() {
                apiRequests
                    .getRequestToApiWithAuthorization('/api/visits/' + this.$route.params.Pid)
                    .then((response) => {
                        this.response = response.data;
                    })
                    .catch(() => {
                            errorHandling.errorMsgWithButton("This appointment no longer exists!")
                        }
                    );
            },
            deleteAppointment(visitId) {
                apiRequests
                    .deleteRequestWithAuthorization('/api/delete/visit/' + visitId)
                    .then(() => {
                        this.$router.push("/userAppointments");
                    })
                    .catch(() => {
                            errorHandling.errorMsgWithButton("Failed to delete this post!");
                        }
                    );
            },
            enableEditingAppointment() {
                this.newDentistName = this.response.dentistName;
                this.newVisitTime = this.response.visitTime;
                this.newProcedure = this.response.procedure;
                this.newDescription = this.response.description;
                this.editingPost = this.response.id;
            },
            disableEditingAppointment: function () {
                this.newDentistName = null;
                this.newVisitTime = null;
                this.newProcedure = null;
                this.newDescription = null;
                this.editingPost = false;
            },
            saveEditAppointment(visitId) {
                this.$validator.validate().then(valid => {
                    if (!valid) {
                        errorHandling.errorMsg("These changes are forbidden!", 1000);
                    } else {
                        this.response.dentistName = this.newDentistName;
                        this.response.visitTime = this.newVisitTime;
                        this.response.procedure = this.newProcedure;
                        this.response.description = this.newDescription;
                        apiRequests
                            .patchRequestWithAuthorization('/api/edit/visit/' + visitId, {
                                dentistName: this.newDentistName,
                                visitTime: this.newVisitTime,
                                procedure: this.newProcedure,
                                description: this.newDescription
                            })
                            .then(() => {
                                errorHandling.successMsg("Appointment successfully updated!", 1200);

                            }).catch((error) => {
                                if (error.response.status === 401 || error.response.status === 403) {
                                    errorHandling.errorMsgWithButton("You need to be logged in to make an update!");
                                }
                                else if(error.response.status === 400) {
                                    errorHandling.errorMsgWithButton("This appointment time is already taken. " +
                                        "Please try a different datetime.")
                                }
                                else {
                                    errorHandling.errorMsgWithButton("Failed to update this post!")
                                }
                            });

                    this.disableEditingAppointment();
                    }
                });
            },

        },
        mounted() {
            //this.formatVisitTime();
            this.loadAppointment();
            let moment = require('moment'); // require
            this.minDate = moment().format("YYYY-MM-DD HH");


        }
    }
</script>

<style scoped>
    .post-item {
        background-color: #f9f9f9;
        border: 4px solid #e9e9e9;
    }

    .reply-area {
        background-color: #f9f9f9;
    }

    .custom-file-upload {
        border: 2px solid gray;
        color: gray;
        background-color: white;
        padding: 8px 20px;
        border-radius: 8px;
        font-size: 20px;
        font-weight: bold;
    }

    input[type=file] {
        display: none;
    }
</style>
