import React from "react";
import { Edit, SimpleForm, TextInput, SelectArrayInput  } from "react-admin";


const roleChoices = [
    { id: "USER", name: "User" },
    { id: "ADMIN", name: "Admin" },
    { id: "MODERATOR", name: "Moderator" },
  ];

const UserEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />
      <TextInput source="name" />
      <TextInput source="email" disabled />
     <SelectArrayInput source="role" choices={roleChoices} /> {/* Allow multiple roles */}
    </SimpleForm>
  </Edit>
);

export default UserEdit;
