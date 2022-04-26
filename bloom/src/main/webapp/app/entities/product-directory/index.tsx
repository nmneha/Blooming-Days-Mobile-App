import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductDirectory from './product-directory';
import ProductDirectoryDetail from './product-directory-detail';
import ProductDirectoryUpdate from './product-directory-update';
import ProductDirectoryDeleteDialog from './product-directory-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductDirectoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductDirectoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductDirectoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductDirectory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductDirectoryDeleteDialog} />
  </>
);

export default Routes;
