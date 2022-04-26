import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProductFeed } from 'app/shared/model/product-feed.model';
import { getEntities } from './product-feed.reducer';

export const ProductFeed = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const productFeedList = useAppSelector(state => state.productFeed.entities);
  const loading = useAppSelector(state => state.productFeed.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="product-feed-heading" data-cy="ProductFeedHeading">
        <Translate contentKey="bloomApp.productFeed.home.title">Product Feeds</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bloomApp.productFeed.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-feed/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bloomApp.productFeed.home.createLabel">Create new Product Feed</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productFeedList && productFeedList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="bloomApp.productFeed.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productFeed.product">Product</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productFeed.productId">Product Id</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productFeed.target">Target</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productFeed.primaryConcern">Primary Concern</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productFeed.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productFeedList.map((productFeed, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-feed/${productFeed.id}`} color="link" size="sm">
                      {productFeed.id}
                    </Button>
                  </td>
                  <td>{productFeed.product}</td>
                  <td>{productFeed.productId}</td>
                  <td>{productFeed.target}</td>
                  <td>{productFeed.primaryConcern}</td>
                  <td>{productFeed.user ? productFeed.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product-feed/${productFeed.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/product-feed/${productFeed.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-feed/${productFeed.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="bloomApp.productFeed.home.notFound">No Product Feeds found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductFeed;
