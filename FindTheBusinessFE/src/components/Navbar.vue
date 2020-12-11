<template>
    <div>
        <nav id="desktop-navigation-bar" class="uk-navbar-container uk-margin uk-visible@m" uk-navbar>

            <div class="uk-navbar-left">
                <a class="uk-navbar-item uk-logo" href="#">Logo</a>
                <a class="uk-navbar-item uk-logo" href="#">Name</a>
            </div>

            <div class="uk-navbar-right custom-padding-navbar">
                <router-link to="/">
                    <button  class="uk-button uk-button-default custom-desktop-nav-button">
                        Acasa
                    </button>
                </router-link>
                <button v-if="!logged" class="uk-button uk-button-default custom-desktop-nav-button" href="#login-sections" uk-toggle>Ofera serviciile tale</button>
                <router-link v-else to="/profile">
                    <button  class="uk-button uk-button-default custom-desktop-nav-button">
                        Profil
                    </button>
                </router-link>
                <button class="uk-button uk-button-default custom-desktop-nav-button" href="#search-sections" uk-toggle>Cauta</button>
                <button v-if="logged === 'ROLE_ADMIN'" class="uk-button uk-button-default custom-desktop-nav-button">Button</button>
            </div>
        </nav>

        <div uk-sticky="sel-target: .uk-navbar-container; cls-active: uk-navbar-sticky">
            <nav id="mobile-navigation-bar" class="uk-navbar-container uk-margin uk-hidden@m custom-bottom-navigation-bar" uk-navbar>
                <div class="uk-navbar-left">
                    <router-link to="/"><button class="uk-button uk-button-default custom-mobile-nav-button">Acasa</button></router-link>
                </div>

                <div class="uk-navbar-center">
                    <button class="uk-button uk-button-default custom-mobile-nav-button" href="#search-sections" uk-toggle>Cauta</button>
                </div>

                <div class="uk-navbar-right">
                    <button v-if="!logged" class="uk-grid-row-small uk-button uk-button-default custom-mobile-nav-button" href="#login-sections" uk-toggle>Ofer</button>
                    <router-link v-else :to="{ name: 'Profile' }">
                        <button class="uk-grid-row-small uk-button uk-button-default custom-mobile-nav-button">
                            Profil
                        </button>
                    </router-link>
                </div>
            </nav>
        </div>

        <SearchDialog />
        <LoginDialog />
        <RegisterDialog />
    </div>
</template>

<script>
const SearchDialog = () => import ('@/components/SearchDialog.vue')
const LoginDialog = () => import ('@/components/LoginDialog.vue')
const RegisterDialog = () => import ('@/components/RegisterDialog.vue')

export default {
    name: "Navbar",
    data() {
        return {
            logged: null
        }
    },

    components: {
        SearchDialog,
        LoginDialog,
        RegisterDialog
    },
    created() {
        return this.$store.watch(
            (state)=>{
                this.logged = this.$store.getters.getLogged
            },
            (newValue, oldValue)=>{
            })
    }
}
</script>

<style lang="scss" scoped>

.uk-logo {
    color: black;
    font-weight: 600;
}

.custom-desktop-nav-button {
    margin-right: 10px;
    border: 2px solid rgba(31, 182, 33, 0.301);
    background-color: #64c316;
    color: black;
    //border: 2px solid #AAAAAA;
    //background-color: black;
    //color: white;
    font-weight: 600;
    border-radius: 20px;
    &:hover, &:focus {
        color: white;
        border: 2px solid rgba(31, 182, 33, 0.301);
    }
}

.custom-mobile-nav-button {
    margin-right: 2px;
    color: black;
    font-weight: 600;
    border: none;
    &:hover, &:focus {
        color:white;
    }
}

#desktop-navigation-bar {
    background-color: #6fc42b;
    //background-color: black;
}

#mobile-navigation-bar {
    background-color: #6fc42b;
    position: fixed;
    left: 0;
    bottom: 0;
    overflow: hidden;
    margin-bottom: 0!important;
    padding: 2%;
    z-index: 100;
    width: 100%;
}

.custom-padding-navbar {
    margin-right: 2%;
}

.custom-bottom-navigation-bar{
    padding-left: 0!important;
    padding-right: 0!important;
}
</style>