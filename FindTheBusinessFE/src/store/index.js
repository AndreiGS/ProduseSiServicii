import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    logged: false,
    hasCompletedTutorial: false,
    tutorialStep: 0,
    tutorialStrings: [
      {
        content: 'Doriti sa treceti prin modul de utilizare? In cazul in care un buton nu este vizibil pe ecranul dumneavostra, glisati degetul pe ecran in sus',
        place: 'top',
        hasYesButton: true,
        hasNoButton: true,
        id: 0,
      },
      {
        content: 'Apasati butonul pentru a deschide fereastra de cumparare credite pentru magazin',
        place: 'bottom',
        id: 1,
      },
      {
        content: 'De aici butonul puteti adauga credite pentru magazin folosind butonul "+" din dreptul creditului pe care il doriti. Acum puteti folosi butonul de inchide pentru a trece la urmatorul pas',
        place: 'bottom',
        id: 2,
      },
      {
        content: 'Apasati butonul pentru a adauga o fatada de magazin',
        place: 'top',
        id: 3,
      },
      {
        content: 'Apasati "Schimba imaginea" pentru a adauga o imagine fatadei de magazin',
        place: 'top',
        id: 4,
      },
      {
        content: 'Apasati butonul "Incarca imagine"',
        place: 'top',
        id: 5,
      },
      {
        content: 'Selectati sectiunea preferata din imagine. Rotiti-o, daca este necesar, iar apoi apasati butonul "Adauga"',
        place: 'top',
        id: 6,
      },
      {
        content: 'Adaugati un nume fatadei de magazin schimband textul "Titlu". Ati terminat? Daca da, apasati butonul de mai jos "DA"',
        place: 'top',
        hasYesButton: true,
        id: 7,
      },
      {
        content: 'Adaugati o descriere fatadei de magazin schimband textul "Descriere". Aceasta trebuie sa aiba maxim 255 de caractere. Ati terminat? Daca da, apasati butonul de mai jos "DA"',
        place: 'top',
        hasYesButton: true,
        id: 8,
      },
      {
        content: 'De aici puteti schimba marimea magazinului, folosind creditele achizitionate',
        place: 'top',
        hasYesButton: true,
        id: 9,
      },
      {
        content: 'Apasati pentru a deschide meniul de schimbare al categoriei si subcategoriilor fatadei de magazin',
        place: 'top',
        id: 10,
      },
      {
        content: 'Alegeti o categorie pentru fatada de magazin. Ati terminat? Daca da, apasati butonul de mai jos "DA"',
        place: 'top',
        hasYesButton: true,
        id: 11,
      },
      {
        content: 'Alegeti pana la 3 subcategorii pentru fatada de magazin, apoi apasati butonul "OK"',
        place: 'top',
        id: 12,
      },
      {
        content: 'Apasati butonul cu o bifa, "Salveaza fatada de magazin" pentru a salva fatada',
        place: 'top',
        id: 13,
      },
      {
        content: 'Apasati butonul cu un creion "Modifica fatada de magazin"',
        place: 'top',
        id: 14,
      },
      {
        content: 'Apasati butonul informativ de pe imagine pentru a afla detalii despre promovare',
        place: 'top',
        id: 15,
      },
      {
        content: 'De aici va puteti promova magazinul (recomandat) pentru a il face mai usor accesibil celorlalti utilizatori! Dupa ce ati terminat, apasati "Inchide"',
        place: 'top',
        id: 16,
      },
      {
        content: 'Apasati butonul rosu pentru a inchide editarea fara a salva modificari legate de fatada de magazin (nume, descriere, subcategorii, credit)',
        place: 'top',
        id: 17,
      },
      {
        content: 'Apasati "Vezi magazin" pentru a intra in fatada de magazin',
        place: 'top',
        id: 18,
      },
      {
        content: 'Apasati butonul cu un creion "Editeaza fatada de magazin" pentru a modifica diferite detalii despre magazin. Toate aceste modificari, explicate, le gasiti in pagina de "Utilizare"',
        place: 'bottom',
        id: 19,
      },
      {
        content: 'Apasati "Schimba detalii contact" pentru ca utilizatorii sa va poata contacta',
        place: 'bottom',
        id: 20,
      },
      {
        content: 'Adaugati toate datele de contact si judetul disponibile pentru firma dumneavoastra apoi apasati "Salveaza"',
        place: 'bottom',
        id: 21,
      },
      {
        content: 'Apasati pe "File"',
        place: 'top',
        id: 22,
      },
      {
        content: 'Apasati pe butonul "+" pentru a adauga o fila',
        place: 'top',
        id: 23,
      },
      {
        content: 'Schimbati numele filei si apasati pe bifa',
        place: 'top',
        id: 24,
      },
      {
        content: 'Apasati pe "Toate"',
        place: 'top',
        id: 25,
      },
      {
        content: 'Apasati butonul pentru a adauga un produs sau serviciu',
        place: 'top',
        id: 26,
      },
      {
        content: 'Apasati butonul "Incarca imaginea"',
        place: 'bottom',
        id: 27,
      },
      {
        content: 'Apasati butonul "Incarca imagine"',
        place: 'top',
        id: 28,
      },
      {
        content: 'Selectati sectiunea preferata din imagine. Rotiti-o, daca este necesar, iar apoi apasati butonul "Adauga"',
        place: 'top',
        id: 29,
      },
      {
        content: 'Adaugati un nume produsului sau serviciului schimband textul "Produs nou". Ati terminat? Daca da, apasati butonul de mai jos "DA"',
        place: 'top',
        id: 30,
        hasYesButton: true,
      },
      {
        content: 'Adaugati o descriere produsului sau serviciului schimband textul "Descriere produs nou". Ati terminat? Daca da, apasati butonul de mai jos "DA"',
        place: 'top',
        id: 31,
        hasYesButton: true,
      },
      {
        content: 'Adaugati un pret(optional), selectati o fila, daca este cazul, iar apoi apasati pe butonul cu o bifa, "Salveaza modificarile"',
        place: 'top',
        id: 32,
      },
      {
        content: 'Felicitari! Tocmai ati adaugat prima fatada de magazin si produs pe contul dumneavoastra. Va recomandam sa cititi si pagina "Utilizare" pentru a intelege toate functionalitatile site-ului',
        place: 'top',
        id: 33,
        hasFinalButton: true
      },
    ]
  },
  mutations: {
    changeLogged(state, loginStatus) {
      state.logged = loginStatus;
      localStorage.setItem("logged", loginStatus);
    },
    changeHasCompletedTutorial(state, hasCompletedTutorial) {
      state.hasCompletedTutorial = hasCompletedTutorial;
      localStorage.setItem("hasCompletedTutorial", hasCompletedTutorial);
    },
    changeTutorialStep(state, step) {
      state.tutorialStep = step;
    },
    initialiseStore(state) {
      state.logged = (localStorage.getItem("logged") != null) ? localStorage.getItem("logged") : false
      state.hasCompletedTutorial = (localStorage.getItem("hasCompletedTutorial") != null) ? localStorage.getItem("hasCompletedTutorial") : false
      state.tutorialStep = 0
    },
  },
  actions: {
    changeLogged(context, loginStatus) {
      context.commit('changeLogged', loginStatus);
    },
    changeTutorialStep(context, tutorialStep) {
      context.commit('changeTutorialStep', tutorialStep);
    },
    changeHasCompletedTutorial(context, hasCompletedTutorial) {
      context.commit('changeHasCompletedTutorial', hasCompletedTutorial);
    },
  },
  modules: {
  },
  getters: {
    getLogged(state) {
      return state.logged;
    },
    getTutorialStep(state) {
      return state.tutorialStep;
    },
    getHasCompletedTutorial(state) {
      return state.hasCompletedTutorial;
    },
    getTutorialStrings(state) {
      return state.tutorialStrings;
    },
  }
})
