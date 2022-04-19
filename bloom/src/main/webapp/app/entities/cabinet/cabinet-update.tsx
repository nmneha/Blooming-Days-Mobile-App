import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProductFeed } from 'app/shared/model/product-feed.model';
import { getEntities as getProductFeeds } from 'app/entities/product-feed/product-feed.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IProductDirectory } from 'app/shared/model/product-directory.model';
import { getEntities as getProductDirectories } from 'app/entities/product-directory/product-directory.reducer';
import { ICabinet } from 'app/shared/model/cabinet.model';
import { getEntity, updateEntity, createEntity, reset } from './cabinet.reducer';

export const CabinetUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const productFeeds = useAppSelector(state => state.productFeed.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const productDirectories = useAppSelector(state => state.productDirectory.entities);
  const cabinetEntity = useAppSelector(state => state.cabinet.entity);
  const loading = useAppSelector(state => state.cabinet.loading);
  const updating = useAppSelector(state => state.cabinet.updating);
  const updateSuccess = useAppSelector(state => state.cabinet.updateSuccess);
  const handleClose = () => {
    props.history.push('/cabinet');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProductFeeds({}));
    dispatch(getUsers({}));
    dispatch(getProductDirectories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cabinetEntity,
      ...values,
      productfeed: productFeeds.find(it => it.id.toString() === values.productfeed.toString()),
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          ...cabinetEntity,
          productfeed: cabinetEntity?.productfeed?.id,
          user: cabinetEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bloomApp.cabinet.home.createOrEditLabel" data-cy="CabinetCreateUpdateHeading">
            <Translate contentKey="bloomApp.cabinet.home.createOrEditLabel">Create or edit a Cabinet</Translate>
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
                  id="cabinet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bloomApp.cabinet.product')}
                id="cabinet-product"
                name="product"
                data-cy="product"
                type="text"
              />
              <ValidatedField
                label={translate('bloomApp.cabinet.productId')}
                id="cabinet-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                id="cabinet-productfeed"
                name="productfeed"
                data-cy="productfeed"
                label={translate('bloomApp.cabinet.productfeed')}
                type="select"
              >
                <option value="" key="0" />
                {productFeeds
                  ? productFeeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="cabinet-user" name="user" data-cy="user" label={translate('bloomApp.cabinet.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cabinet" replace color="info">
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

export default CabinetUpdate;
