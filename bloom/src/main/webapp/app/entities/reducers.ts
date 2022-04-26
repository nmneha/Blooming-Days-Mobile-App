import productDirectory from 'app/entities/product-directory/product-directory.reducer';
import cabinet from 'app/entities/cabinet/cabinet.reducer';
import productFeed from 'app/entities/product-feed/product-feed.reducer';
import comments from 'app/entities/comments/comments.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  productDirectory,
  cabinet,
  productFeed,
  comments,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
