import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductDirectory from './product-directory';
import Cabinet from './cabinet';
import ProductFeed from './product-feed';
import Comments from './comments';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}product-directory`} component={ProductDirectory} />
        <ErrorBoundaryRoute path={`${match.url}cabinet`} component={Cabinet} />
        <ErrorBoundaryRoute path={`${match.url}product-feed`} component={ProductFeed} />
        <ErrorBoundaryRoute path={`${match.url}comments`} component={Comments} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
