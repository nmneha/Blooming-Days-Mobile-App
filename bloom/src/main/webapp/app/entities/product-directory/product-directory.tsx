import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProductDirectory } from 'app/shared/model/product-directory.model';
import { getEntities } from './product-directory.reducer';

export const ProductDirectory = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const productDirectoryList = useAppSelector(state => state.productDirectory.entities);
  const loading = useAppSelector(state => state.productDirectory.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="product-directory-heading" data-cy="ProductDirectoryHeading">
        <Translate contentKey="bloomApp.productDirectory.home.title">Product Directories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bloomApp.productDirectory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-directory/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bloomApp.productDirectory.home.createLabel">Create new Product Directory</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productDirectoryList && productDirectoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="bloomApp.productDirectory.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productDirectory.product">Product</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productDirectory.productId">Product Id</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productDirectory.productBrand">Product Brand</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productDirectory.primaryIngredient">Primary Ingredient</Translate>
                </th>
                <th>
                  <Translate contentKey="bloomApp.productDirectory.cabinet">Cabinet</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productDirectoryList.map((productDirectory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-directory/${productDirectory.id}`} color="link" size="sm">
                      {productDirectory.id}
                    </Button>
                  </td>
                  <td>{productDirectory.product}</td>
                  <td>{productDirectory.productId}</td>
                  <td>{productDirectory.productBrand}</td>
                  <td>{productDirectory.primaryIngredient}</td>
                  <td>
                    {productDirectory.cabinets
                      ? productDirectory.cabinets.map((val, j) => (
                          <span key={j}>
                            <Link to={`/cabinet/${val.id}`}>{val.id}</Link>
                            {j === productDirectory.cabinets.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/product-directory/${productDirectory.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-directory/${productDirectory.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product-directory/${productDirectory.id}/delete`}
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
              <Translate contentKey="bloomApp.productDirectory.home.notFound">No Product Directories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductDirectory;
