import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductFeed from './product-feed';
import ProductFeedDetail from './product-feed-detail';
import ProductFeedUpdate from './product-feed-update';
import ProductFeedDeleteDialog from './product-feed-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductFeedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductFeedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductFeedDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductFeed} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductFeedDeleteDialog} />
  </>
);

export default Routes;
