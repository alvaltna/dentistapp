<template>
    <div class="input-group justify-content-lg-end justify-content-center">
      <input type="search" class="form-control search-input" placeholder="Search" aria-label="Search"
             aria-describedby="basic-addon2" v-model="searchTerm">
      <div class="input-group-append">
        <button class="btn btn-dark" type="button" @click="searchFunction()">Search</button>
      </div>
    </div>
</template>

<script>
    export default {
        name: "Search",
        data() {
            return {
                searchTerm: ''
            }
        },
        methods: {
            searchFunction() {
                this.$router.push({name: 'search', params: {searchTerm: this.searchTerm}});
            }
        }
    }
</script>

<style scoped>
  .search-input {
    border: white;
    max-width: 300px;
  }

  input[type="search"]:focus {
    background-color: #dddddd;
  }

  input[type="search"]:hover {
    border: 1px solid #333;
  }
</style>