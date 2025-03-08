import React from "react";
import { Admin, Resource } from "react-admin";
import simpleRestProvider from "ra-data-simple-rest";
import { MovieList, MovieCreate, MovieEdit } from "./Movies";

const dataProvider = simpleRestProvider("http://localhost:8080/api");

const AdminPanel = () => {
    return (
        <Admin dataProvider={dataProvider}>
            <Resource name="movies" list={MovieList} create={MovieCreate} edit={MovieEdit} />
        </Admin>
    );
};

export default AdminPanel;
