import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/product-directory">
        <Translate contentKey="global.menu.entities.productDirectory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cabinet">
        <Translate contentKey="global.menu.entities.cabinet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product-feed">
        <Translate contentKey="global.menu.entities.productFeed" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/comments">
        <Translate contentKey="global.menu.entities.comments" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;
