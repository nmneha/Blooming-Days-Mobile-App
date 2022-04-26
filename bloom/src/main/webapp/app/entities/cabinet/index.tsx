import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cabinet from './cabinet';
import CabinetDetail from './cabinet-detail';
import CabinetUpdate from './cabinet-update';
import CabinetDeleteDialog from './cabinet-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CabinetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CabinetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CabinetDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cabinet} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CabinetDeleteDialog} />
  </>
);

export default Routes;
