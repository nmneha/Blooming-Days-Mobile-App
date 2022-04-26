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
import { IProductDirectory } from 'app/shared/model/product-directory.model';
import { getEntity, updateEntity, createEntity, reset } from './product-directory.reducer';

export const ProductDirectoryUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cabinets = useAppSelector(state => state.cabinet.entities);
  const productDirectoryEntity = useAppSelector(state => state.productDirectory.entity);
  const loading = useAppSelector(state => state.productDirectory.loading);
  const updating = useAppSelector(state => state.productDirectory.updating);
  const updateSuccess = useAppSelector(state => state.productDirectory.updateSuccess);
  const handleClose = () => {
    props.history.push('/product-directory');
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
      ...productDirectoryEntity,
      ...values,
      cabinets: mapIdList(values.cabinets),
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
          ...productDirectoryEntity,
          cabinets: productDirectoryEntity?.cabinets?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bloomApp.productDirectory.home.createOrEditLabel" data-cy="ProductDirectoryCreateUpdateHeading">
            <Translate contentKey="bloomApp.productDirectory.home.createOrEditLabel">Create or edit a ProductDirectory</Translate>
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
                  id="product-directory-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bloomApp.productDirectory.product')}
                id="product-directory-product"
                name="product"
                data-cy="product"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.productDirectory.productId')}
                id="product-directory-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.productDirectory.productBrand')}
                id="product-directory-productBrand"
                name="productBrand"
                data-cy="productBrand"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.productDirectory.primaryIngredient')}
                id="product-directory-primaryIngredient"
                name="primaryIngredient"
                data-cy="primaryIngredient"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.productDirectory.cabinet')}
                id="product-directory-cabinet"
                data-cy="cabinet"
                type="select"
                multiple
                name="cabinets"
              >
                <option value="" key="0" />
                {cabinets
                  ? cabinets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-directory" replace color="info">
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

export default ProductDirectoryUpdate;
