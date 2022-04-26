import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cabinet.reducer';

export const CabinetDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cabinetEntity = useAppSelector(state => state.cabinet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cabinetDetailsHeading">
          <Translate contentKey="bloomApp.cabinet.detail.title">Cabinet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cabinetEntity.id}</dd>
          <dt>
            <span id="product">
              <Translate contentKey="bloomApp.cabinet.product">Product</Translate>
            </span>
          </dt>
          <dd>{cabinetEntity.product}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="bloomApp.cabinet.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{cabinetEntity.productId}</dd>
          <dt>
            <Translate contentKey="bloomApp.cabinet.productfeed">Productfeed</Translate>
          </dt>
          <dd>{cabinetEntity.productfeed ? cabinetEntity.productfeed.id : ''}</dd>
          <dt>
            <Translate contentKey="bloomApp.cabinet.user">User</Translate>
          </dt>
          <dd>{cabinetEntity.user ? cabinetEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/cabinet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cabinet/${cabinetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CabinetDetail;
