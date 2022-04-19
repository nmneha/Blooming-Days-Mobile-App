import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICabinet } from 'app/shared/model/cabinet.model';
import { getEntities as getCabinets } from 'app/entities/cabinet/cabinet.reducer';
import { IProductFeed } from 'app/shared/model/product-feed.model';
import { getEntity, updateEntity, createEntity, reset } from './product-feed.reducer';

export const ProductFeedUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cabinets = useAppSelector(state => state.cabinet.entities);
  const productFeedEntity = useAppSelector(state => state.productFeed.entity);
  const loading = useAppSelector(state => state.productFeed.loading);
  const updating = useAppSelector(state => state.productFeed.updating);
  const updateSuccess = useAppSelector(state => state.productFeed.updateSuccess);
  const handleClose = () => {
    props.history.push('/product-feed');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCabinets({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...productFeedEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...productFeedEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bloomApp.productFeed.home.createOrEditLabel" data-cy="ProductFeedCreateUpdateHeading">
            <Translate contentKey="bloomApp.productFeed.home.createOrEditLabel">Create or edit a ProductFeed</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="product-feed-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bloomApp.productFeed.product')}
                id="product-feed-product"
                name="product"
                data-cy="product"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.productFeed.productId')}
                id="product-feed-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.productFeed.target')}
                id="product-feed-target"
                name="target"
                data-cy="target"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.productFeed.primaryConcern')}
                id="product-feed-primaryConcern"
                name="primaryConcern"
                data-cy="primaryConcern"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-feed" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProductFeedUpdate;
