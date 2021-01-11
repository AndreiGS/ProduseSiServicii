<template>
  <vk-card padding="small" class="card">
    <div style="padding-left: 0 !important; text-align: left;">
      <vk-grid>
        <div class="uk-width-1-3@m">
          <p style="font-size: 13px; text-align: right;">
            Postat in data de {{date}}
          </p>
          <StarRating
              class="uk-flex uk-flex-right"
              :rating="rating"
              :show-rating="false"
              :star-size="25"
              :increment="0.1"
              :read-only="true"
          />
        </div>
        <div class="uk-width-2-3@m uk-flex-first@m">
          <vk-card-title>{{ user }}</vk-card-title>
          <p>{{ comment }}</p>
        </div>
      </vk-grid>
    </div>
  </vk-card>
</template>

<script>
const StarRating = () => import(/* webpackChunkName: "others-chunk" */ 'vue-star-rating');

export default {
    name: "CommentCard",
    components: {
        StarRating
    },
    props: {
        user: "",
        rating: null,
        comment: "",
        date: null
    }
}
</script>
