<template>
  <div class="uk-padding-small" style="background-color: rgba(111,196,43,0.2)">
    <div v-if="loading == true">
        Se incarca
    </div>
    <div v-else>
        <AddCommentCard class="add-style" v-if="isOwner == false" v-on:reload_rating="$emit('reload_rating')" v-on:reload_comments="getComments()" :code="this.code"/>
        <div v-if="error == true || comments == null" class="not-found">
            Nu au fost gasite comentarii
        </div>
        <div v-else>
            <CommentCard v-for="comment in comments" :key="comment.id"
                :user = comment.email
                :rating = comment.rating
                :comment = comment.text
                :date = comment.postDate
            />
        </div>
    </div>
  </div>
</template>

<script>
const CommentCard = () => import(/* webpackChunkName: "cards-chunk" */"@/components/CommentCard");
const AddCommentCard = () => import(/* webpackChunkName: "cards-chunk" */ "@/components/AddCommentCard");
import axios from 'axios'

export default {
    name: "CommentCardGrid",
    components: {
        CommentCard,
        AddCommentCard
    },
    props: {
        code: null,
        isOwner: null
    },
    data() {
        return {
            comments: null,
            loading: true,
            error: false,

            backend: process.env.VUE_APP_BACKEND || 'http://localhost:8080'
        }
    },
    methods: {
        async getComments() {
          var timeoutVar = setTimeout(() => { this.loading = true; }, 1000);
          await axios({
            method: 'get',
            url: this.backend+"/api/comments/getComments?store="+this.$route.params.id  
          })
            .then((response) => {
              this.comments = response.data.comments
              if(this.comments.length == 0)
                this.comments = null;
            })
            .catch((error) => {
              this.error = true;
            })
            .finally(() => {
              this.loading = false;
              clearTimeout(timeoutVar)
            }) 
        }
    },
    created() {
        this.getComments();
    } 
}
</script>

<style lang="scss" scoped>
.not-found {
  font-size: 20px;
}
.add-style {
  margin-bottom: 15px;
}
</style>