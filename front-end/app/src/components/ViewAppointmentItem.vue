<template>
  <div class="post-list-item px-2 pt-2" @click="goToDetail(data.id)">
    <h3 class="text-left">{{ data.dentistName }}</h3>
    <p class="text-left">{{ this.dateTime }}</p>
  </div>
</template>

<script>
    export default {
        name: "ViewAppointmentItem",
        props: ['data'],
        data() {
          return {
            dateTime: ''

          };
        },
        methods: {
            goToDetail(proId) {
                this.$router.push({name: 'viewAppointment', params: {Pid: proId}})
            },

            formatVisitTime(){

              this.dateTime = this.$props.data.visitTime.split("T")[0] + ' '
                      + this.$props.data.visitTime.split("T")[1]

            }
        },

      mounted() {
        this.formatVisitTime();
      }
    }
</script>

<style scoped>
  .post-list-item {
    border-bottom: 2px solid #e9e9e9;
    cursor: pointer;
  }

  .post-list-item:hover {
    background-color: #f6f6f6;
  }

  .post-list-item:active {
    background-color: #f3f3f3;
  }
</style>