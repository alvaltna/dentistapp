<template>
  <div>
    <div class="container-fluid">
      <div class="row justify-content-center">

        <div class="col-lg-9 col-md-10 col-sm-12 px-0 divider">
          <div class="col-xl-4 col-lg-5 col-md-6 col-sm-8 input-group px-0 float-right order-by-form">
            <select class="custom-select border-0" id="inputGroupSelect" v-model="sortBy">
              <option disabled value="">Sort by...</option>
              <option>Latest appointment first</option>
              <option>Earliest appointment first</option>
              <option>Dentist name alphabetically descending</option>
              <option>Dentist name alphabetically ascending</option>
            </select>
            <div class="input-group-append border-0">
              <button class="btn btn-secondary border-0" type="button" @click="sort()">Sort</button>
            </div>
          </div>
        </div>

        <div class="col-lg-9 col-md-10 col-sm-12 px-0">
          <ViewAppointmentItem v-for="data in response"
                         :data="data"
                         :key="data.id"/>
        </div>
      </div>
    </div>

    <br>
    <button class="mr-1 ml-1 mb-3 btn btn-lg btn-dark" v-if="currentPageNum > 0" v-on:click="prevPage()">
      Previous
    </button>
    <button class="mr-1 ml-1 mb-3 btn btn-lg btn-dark" v-if="currentPageNum < numOfPages" v-on:click="nextPage()">
      Next
    </button>
    <br>
  </div>
</template>

<script>
    import apiRequests from '../javascript/apiRequests.js';
    import ViewAppointmentItem from './ViewAppointmentItem.vue'

    export default {
        name: 'viewAppointments',
        components: {
            ViewAppointmentItem
        },
        methods: {
            loadContent() {
                this.currentPageNum = 0;

                this.searchTerm = this.$route.params.searchTerm;
                if (this.searchTerm !== null && this.searchTerm !== '' && this.searchTerm !== undefined) {
                    this.baseUrl = '/api/visits/find?searchTerm=' + this.searchTerm;
                } else {
                    this.baseUrl = '/api/visits?dentistName=' + this.dentistName;
                }

                this.dataRequest(this.baseUrl);
            },
            nextPage() {
                this.currentPageNum += 1;
                let url = this.getUrl() + '&page=' + this.currentPageNum;
                this.dataRequest(url);
            },
            prevPage() {
                this.currentPageNum -= 1;
                let url = this.getUrl() + '&page=' + this.currentPageNum;
                this.dataRequest(url);
            },
            getUrl() {
                let url = this.baseUrl;

                if (this.sortBy !== '') {
                    let sortKeyword;
                    let sortDirection;

                    if (this.sortBy === 'Latest appointment first') {
                        sortKeyword = 'visitTime';
                        sortDirection = 'descending';
                    } else if (this.sortBy === 'Earliest appointment first') {
                        sortKeyword = 'visitTime';
                        sortDirection = 'ascending';
                    } else if (this.sortBy === 'Dentist name alphabetically descending') {
                        sortKeyword = 'dentistName';
                        sortDirection = 'descending';
                    } else if (this.sortBy === 'Dentist name alphabetically ascending') {
                        sortKeyword = 'dentistName';
                        sortDirection = 'ascending';
                    }
                    url += '&sortBy=' + sortKeyword + '&order=' + sortDirection;
                }

                return url
            },
            sort() {
                this.dataRequest(this.getUrl());
            },
            dataRequest(url) {
                apiRequests.getRequestToApiWithAuthorization(url)
                    .then(result => {
                        this.response = result.data.visit;

                        if (result.data.amountOfPages <= 0) {
                            this.numOfPages = 0;
                        } else {
                            this.numOfPages = result.data.amountOfPages - 1;
                        }
                    });
            },
        },
        watch: {
            $route() {
                this.loadContent();
            }
        },
        data() {
            return {
                response: [],
                numOfPages: 0,
                currentPageNum: 0,
                dentistName: 'all',
                sortBy: '',
                searchTerm: null,
                baseUrl: ''
            };
        },
        mounted() {
            this.loadContent();
        }
    }
</script>

<style scoped>
  /*@import './../css/postsView.css';*/

  .divider {
    border-bottom: 4px solid #e5e5e5;
  }

  .order-by-form {
    border-top: 4px solid #e5e5e5;
    border-right: 4px solid #e5e5e5;
    border-left: 4px solid #e5e5e5;
    border-radius: 0;
    background-color: #f6f6f6;
  }

  .border-0 {
    border-radius: 0 !important;
  }
</style>
