<template>
  <div>
    <p><b>TELEFON: </b> {{(phone != null) ? phone : '-'}}</p>
    <p><b>ADRESA: </b> {{(address != null) ? address : '-'}}</p>
    <p><b>EMAIL: </b> {{(email != null) ? email : '-'}}</p>
    <p><b>WEBSITE: </b> {{(websiteLink != null) ? websiteLink : '-'}}</p>
    <p><b>JUDET: </b> {{(county != null) ? county : '-'}}</p>
  </div>
</template>

<script>
export default {
  name: "ContactData",
  props: {
    phone: null,
    address: null,
    email: null,
    websiteLink: null,
    county: null
  }
}
</script>

<style scoped>

</style>