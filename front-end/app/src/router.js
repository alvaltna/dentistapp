import Vue from 'vue'
import Router from 'vue-router'
import MakeAppointment from './components/MakeAppointment.vue'
import ViewAppointment from './components/ViewAppointment.vue'
import Registration from './components/Registration.vue'
import ViewAppointments from '@/components/ViewAppointments.vue'
import ForgotPassword from '@/components/ForgotPassword.vue'
import ResetPassword from '@/components/ResetPassword.vue'
import Home from '@/components/Home.vue'


Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            name: "home",
            component: Home
        },
        {
            path: '/makeAppointment',
            name: 'makeAppointment',
            component: MakeAppointment
        },
        {
            path: '/search=:searchTerm',
            name: 'search',
            component: ViewAppointments
        },
        {
            path: '/userAppointments',
            name: 'viewAppointments',
            component: ViewAppointments
        },
        {
            path: '/ViewAppointment/:Pid',
            name: 'viewAppointment',
            component: ViewAppointment
        },
        {
            path: '/registration/',
            name: 'registration',
            component: Registration
        },
        {
            path: '/forgotPassword',
            name: 'forgotPassword',
            component: ForgotPassword
        },
        {
            path: '/resetPassword',
            name: 'resetPassword',
            component: ResetPassword
        }
    ]
})
