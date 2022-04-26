import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICabinet } from 'app/shared/model/cabinet.model';
import { getEntities } from './cabinet.reducer';

export const Cabinet = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const cabinetList = useAppSelector(state => state.cabinet.entities);
  const loading = useAppSelector(state => state.cabinet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="cabinet-heading" data-cy="CabinetHeading">
        <Translate contentKey="bloomApp.cabinet.home.title">Cabinets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bloomApp.cabinet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cabinet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bloomApp.cabinet.home.createLabel">Create new Cabinet</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cabinetList && cabinetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="bloomApp.cabinet.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.cabinet.product">Product</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.cabinet.productId">Product Id</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.cabinet.productfeed">Productfeed</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.cabinet.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cabinetList.map((cabinet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cabinet/${cabinet.id}`} color="link" size="sm">
                      {cabinet.id}
                    </Button>
                  </td>
                  <td>{cabinet.product}</td>
                  <td>{cabinet.productId}</td>
                  <td>{cabinet.productfeed ? <Link to={`/product-feed/${cabinet.productfeed.id}`}>{cabinet.productfeed.id}</Link> : ''}</td>
                  <td>{cabinet.user ? cabinet.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/cabinet/${cabinet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/cabinet/${cabinet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/cabinet/${cabinet.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="bloomApp.cabinet.home.notFound">No Cabinets found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Cabinet;
