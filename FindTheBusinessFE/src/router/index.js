import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import(/* webpackChunkName: "home-chunk" */ '../views/Home.vue'),
  },
  {
    path: '/terms',
    name: 'TermsAndConditions',
    component: () => import(/* webpackChunkName: "others-chunk" */ '../views/TermsAndConditions.vue'),
  },
  {
    path: '/tutorial',
    name: 'Tutorial',
    component: () => import(/* webpackChunkName: "others-chunk" */ '../views/Tutorial.vue'),
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import(/* webpackChunkName: "home-chunk" */ '../views/Home.vue'),
    props: (route) => ({ 
      category: route.query.category,
      subcategory: route.query.subcategory,
      search: route.query.search,
      rating: route.query.rating,
      county: route.query.county
    })
  },
  {
    path: '/store/:id?',
    name: 'Storefront',
    component: () => import(/* webpackChunkName: "storefront-chunk" */ '../views/Storefront.vue'),
    props: (route) => ({ 
      code: route.query.code,
      state: route.query.state,
      owner: (route.query.owner != undefined) ?  route.query.owner : false,
      itemsIds: route.query.itemsIds
    })
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import(/* webpackChunkName: "profile-chunk" */ '../views/Profile.vue'),
    
  },
  {
    path: '/changed',
    name: 'ChangedInformations',
    component: () => import(/* webpackChunkName: "informations-chunk" */ '../views/ChangedInformations.vue'),
    props: (route) => ({ 
      code: route.query.code 
    })
  },
  {
    path: '/confirmEmail',
    name: 'AccountConfirmation',
    component: () => import(/* webpackChunkName: "informations-chunk" */ '../views/AccountConfirmation.vue'),
    props: (route) => ({ 
      code: route.query.code 
    })

  },
  {
    path: '/changePassword',
    name: 'PasswordChangingConfirmation',
    component: () => import(/* webpackChunkName: "informations-chunk" */ '../views/PasswordChangingConfirmation.vue'),
    props:  (route) => ({ 
      code: route.query.code 
    })
  },
  {
    path: '/resetPassword',
    name: 'ResetPassword',
    component: () => import(/* webpackChunkName: "informations-chunk" */ '../views/ResetPassword.vue'),
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
