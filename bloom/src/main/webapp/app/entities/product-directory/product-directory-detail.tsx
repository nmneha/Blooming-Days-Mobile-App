import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-directory.reducer';

export const ProductDirectoryDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const productDirectoryEntity = useAppSelector(state => state.productDirectory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDirectoryDetailsHeading">
          <Translate contentKey="bloomApp.productDirectory.detail.title">ProductDirectory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productDirectoryEntity.id}</dd>
          <dt>
            <span id="product">
              <Translate contentKey="bloomApp.productDirectory.product">Product</Translate>
            </span>
          </dt>
          <dd>{productDirectoryEntity.product}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="bloomApp.productDirectory.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productDirectoryEntity.productId}</dd>
          <dt>
            <span id="productBrand">
              <Translate contentKey="bloomApp.productDirectory.productBrand">Product Brand</Translate>
            </span>
          </dt>
          <dd>{productDirectoryEntity.productBrand}</dd>
          <dt>
            <span id="primaryIngredient">
              <Translate contentKey="bloomApp.productDirectory.primaryIngredient">Primary Ingredient</Translate>
            </span>
          </dt>
          <dd>{productDirectoryEntity.primaryIngredient}</dd>
          <dt>
            <Translate contentKey="bloomApp.productDirectory.cabinet">Cabinet</Translate>
          </dt>
          <dd>
            {productDirectoryEntity.cabinets
              ? productDirectoryEntity.cabinets.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {productDirectoryEntity.cabinets && i === productDirectoryEntity.cabinets.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-directory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-directory/${productDirectoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDirectoryDetail;
