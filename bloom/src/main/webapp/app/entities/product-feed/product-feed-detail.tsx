import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-feed.reducer';

export const ProductFeedDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const productFeedEntity = useAppSelector(state => state.productFeed.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productFeedDetailsHeading">
          <Translate contentKey="bloomApp.productFeed.detail.title">ProductFeed</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productFeedEntity.id}</dd>
          <dt>
            <span id="product">
              <Translate contentKey="bloomApp.productFeed.product">Product</Translate>
            </span>
          </dt>
          <dd>{productFeedEntity.product}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="bloomApp.productFeed.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productFeedEntity.productId}</dd>
          <dt>
            <span id="target">
              <Translate contentKey="bloomApp.productFeed.target">Target</Translate>
            </span>
          </dt>
          <dd>{productFeedEntity.target}</dd>
          <dt>
            <span id="primaryConcern">
              <Translate contentKey="bloomApp.productFeed.primaryConcern">Primary Concern</Translate>
            </span>
          </dt>
          <dd>{productFeedEntity.primaryConcern}</dd>
          <dt>
            <Translate contentKey="bloomApp.productFeed.user">User</Translate>
          </dt>
          <dd>{productFeedEntity.user ? productFeedEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-feed" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-feed/${productFeedEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductFeedDetail;
