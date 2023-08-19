create sequence address_id_seq start 1 increment 1;
create sequence app_user_id_seq start 1 increment 1;
create sequence appointments_id_seq start 1 increment 1;
create sequence fichaavaliacao_id_seq start 1 increment 1;
create sequence gender_id_seq start 1 increment 1;
create sequence organization_id_seq start 1 increment 1;
create sequence personalactivity_id_seq start 1 increment 1;
create sequence pessoa_id_seq start 1 increment 1;
create sequence profile_id_seq start 1 increment 1;
create sequence profileaccess_id_seq start 1 increment 1;
create sequence role_id_seq start 1 increment 1;
create sequence routine_id_seq start 1 increment 1;
create sequence serviceType_id_seq start 1 increment 1;
create sequence statusAgendamento_id_seq start 1 increment 1;

    create table address (
       id int8 not null,
        active boolean,
        city varchar(255),
        complement varchar(255),
        deletedAt timestamp,
        number int8,
        publicPlace varchar(255),
        state varchar(255),
        updatedAt timestamp,
        zipCode varchar(255),
        userId int8,
        user_id int8,
        primary key (id)
    );

    create table app_user (
       id int8 not null,
        active boolean,
        bot boolean,
        changePassword boolean,
        deletedAt timestamp,
        login varchar(255) not null,
        password varchar(255) not null,
        professionalName varchar(255),
        updatedAt timestamp,
        updatedBy varchar(255),
        user varchar(255),
        organizationDefaultId int8,
        personId int8,
        primary key (id)
    );

    create table appointments (
       id int8 not null,
        active boolean,
        appointmentDate date,
        appointmentTime time,
        deletedAt timestamp,
        personName varchar(255),
        preference boolean,
        professionalName varchar(255),
        updatedAt timestamp,
        appointmentsOldId int8,
        bookingStatusId int8,
        organizationId int8,
        personId int8,
        professionalId int8,
        serviceType int8,
        userId int8,
        primary key (id)
    );

    create table bookingStatus (
       id int8 not null,
        status varchar(255),
        primary key (id)
    );

    create table gender (
       id int8 not null,
        gender varchar(255),
        primary key (id)
    );

    create table organization (
       id int8 not null,
        active boolean,
        cellphone varchar(255),
        deletedAt timestamp,
        email varchar(255),
        identityDocument varchar(255),
        name varchar(255),
        telephone varchar(255),
        updatedAt timestamp,
        addressId int8,
        personal_activity_id int8,
        serviceType_id int8,
        userId int8,
        user_id int8,
        primary key (id)
    );

    create table person (
       id int8 not null,
        active boolean,
        birthday date,
        cellphone varchar(255),
        deletedAt timestamp,
        document varchar(255),
        email varchar(255),
        name varchar(255),
        telephone varchar(255),
        updatedAt timestamp,
        addressId int8,
        generoId int8,
        userId int8,
        primary key (id)
    );

    create table personalActivity (
       id int8 not null,
        active boolean,
        deletedAt timestamp,
        occupation varchar(255),
        personName varchar(255),
        responsibleContact varchar(255),
        updatedAt timestamp,
        personId int8,
        userId int8,
        primary key (id)
    );

    create table profile (
       id int8 not null,
        createAt timestamp,
        fileReference varchar(255),
        fileSize int8,
        keyName varchar(255),
        mimetype varchar(255),
        originalName varchar(255),
        personName varchar(255),
        personalactivityId int8,
        primary key (id)
    );

    create table profileAccess (
       id int8 not null,
        create boolean,
        delete boolean,
        name varchar(255),
        read boolean,
        update boolean,
        updatedAt timestamp,
        userId int8,
        primary key (id)
    );

    create table role (
       id int8 not null,
        admin boolean,
        role varchar(255),
        user_id int8,
        primary key (id)
    );

    create table routine (
       id int8 not null,
        icon varchar(255),
        name varchar(255),
        path varchar(255),
        title varchar(255),
        updatedAt timestamp,
        userId int8,
        primary key (id)
    );

    create table routineprofileaccess (
       profileaccessId int8 not null,
        routineId int8 not null
    );

    create table serviceType (
       id int8 not null,
        serviceType varchar(255),
        primary key (id)
    );

    create table serviceTypeorganizations (
       serviceTypeId int8 not null,
        organizationId int8 not null
    );

    create table servicetypesusers (
       professionalId int8 not null,
        serviceTypeId int8 not null
    );

    create table task (
       id int8 not null,
        active boolean,
        avaliation varchar(255),
        deletedAt timestamp,
        summary varchar(255),
        task varchar(255),
        taskDate timestamp,
        updatedAt timestamp,
        personalactivityId int8,
        userId int8,
        primary key (id)
    );

    create table userorganization (
       userId int8 not null,
        organizationId int8 not null
    );

    create table userroles (
       userId int8 not null,
        roleId int8 not null
    );
create index iuserak1 on app_user (personId, login, organizationDefaultId, active);
create index iappointmentsak1 on appointments (appointmentDate, appointmentTime, personId, professionalId, organizationId, bookingStatusId, active);
create index ipessoaak1 on person (name, telephone, cellphone, birthday, document, active);
create index ipersonalActivityak1 on personalActivity (personId, active);
create index ifichaavaliacaoak1 on task (taskDate, personalactivityId, active);

    alter table if exists address
       add constraint FKigv0mculvsdbsj25spun5ovv5
       foreign key (userId)
       references app_user;

    alter table if exists address
       add constraint FKe4etc0n243br1d15pcmbt930w
       foreign key (user_id)
       references app_user;

    alter table if exists app_user
       add constraint FKpjn3y2n1axn7hujk5bija9ax4
       foreign key (organizationDefaultId)
       references organization;

    alter table if exists app_user
       add constraint FKiqkswjmtqneo4qyll8lu865e0
       foreign key (personId)
       references person;

    alter table if exists appointments
       add constraint FKtmwsbjwwhomi9ld461f520hd0
       foreign key (appointmentsOldId)
       references appointments;

    alter table if exists appointments
       add constraint FKlu4p4h5vfnbx3nq4pvkupmiyk
       foreign key (bookingStatusId)
       references bookingStatus;

    alter table if exists appointments
       add constraint FK77rpceha7dpe8ovdiswc0ekb4
       foreign key (organizationId)
       references organization;

    alter table if exists appointments
       add constraint FKml38u0jykei620yr6i3yp1cv2
       foreign key (personId)
       references person;

    alter table if exists appointments
       add constraint FKrftgxhf6nmxu75aonabbo1wmc
       foreign key (professionalId)
       references app_user;

    alter table if exists appointments
       add constraint FKa5ev07ejnnpmjj600ri45vy83
       foreign key (serviceType)
       references serviceType;

    alter table if exists appointments
       add constraint FKknsajpu07otwi7c9texgl7axh
       foreign key (userId)
       references app_user;

    alter table if exists organization
       add constraint FKkwxrfy6drw2u2u57thawy3yc1
       foreign key (addressId)
       references address;

    alter table if exists organization
       add constraint FKkbgqvwfgnsp44hxyt2vjytwba
       foreign key (personal_activity_id)
       references personalActivity;

    alter table if exists organization
       add constraint FK3f61sx00inmu4673a6jlpshu5
       foreign key (serviceType_id)
       references serviceType;

    alter table if exists organization
       add constraint FKpc5whytqbpbxcpyd9008iys4o
       foreign key (userId)
       references app_user;

    alter table if exists organization
       add constraint FK8cge1wrx5wriwwilhlmt32ujx
       foreign key (user_id)
       references app_user;

    alter table if exists person
       add constraint FKf0h7aqcynkctw6mq6b058q6o7
       foreign key (addressId)
       references address;

    alter table if exists person
       add constraint FKhpnyloptj1qbx64sscfng1jq9
       foreign key (generoId)
       references gender;

    alter table if exists person
       add constraint FKkkj0qykkhqgwow8fxndix92fy
       foreign key (userId)
       references app_user;

    alter table if exists personalActivity
       add constraint FKlga9l4xc7k1ofw67lv8flrj90
       foreign key (personId)
       references person;

    alter table if exists personalActivity
       add constraint FKsu171inm186b5mnrjlxmle27x
       foreign key (userId)
       references app_user;

    alter table if exists profile
       add constraint FKt1hgtnrlx0swokohm9ieno4f7
       foreign key (personalactivityId)
       references personalActivity;

    alter table if exists profileAccess
       add constraint FK95r3o0jss0iqix9797o76dkiq
       foreign key (userId)
       references app_user;

    alter table if exists role
       add constraint FK4dhnthkjee7rc3spnhu8ed3rc
       foreign key (user_id)
       references app_user;

    alter table if exists routine
       add constraint FK35ihrtpjx8c8j26dx7tvnafyw
       foreign key (userId)
       references app_user;

    alter table if exists routineprofileaccess
       add constraint FK84klusi0bmdi5ccgcxpws2u5m
       foreign key (routineId)
       references routine;

    alter table if exists routineprofileaccess
       add constraint FKiybb96wd7r665k7wkvw1co85e
       foreign key (profileaccessId)
       references profileAccess;

    alter table if exists serviceTypeorganizations
       add constraint FK89diew1dm2ilc9lqp9cd1vv60
       foreign key (organizationId)
       references organization;

    alter table if exists serviceTypeorganizations
       add constraint FKh8rhfmkl7ld8ahj48rqp3ow4r
       foreign key (serviceTypeId)
       references serviceType;

    alter table if exists servicetypesusers
       add constraint FKcal1gj4i0gajylcoekpfbjtuk
       foreign key (serviceTypeId)
       references serviceType;

    alter table if exists servicetypesusers
       add constraint FKmbud71b01ubfmob0wrjtt9us6
       foreign key (professionalId)
       references app_user;

    alter table if exists task
       add constraint FK577p2tn7uc72latlunjdogl3s
       foreign key (personalactivityId)
       references personalActivity;

    alter table if exists task
       add constraint FKk4f4itlykev1j3xxoms1c0jk0
       foreign key (userId)
       references app_user;

    alter table if exists userorganization
       add constraint FKihxv5q1k4mfom5soj5mjmyg6t
       foreign key (organizationId)
       references organization;

    alter table if exists userorganization
       add constraint FKc30qpfm1xv6pvbqt6as0tqum4
       foreign key (userId)
       references app_user;

    alter table if exists userroles
       add constraint FKpyduh9ia2fnx3o8pc3eilc3ha
       foreign key (roleId)
       references role;

    alter table if exists userroles
       add constraint FK1n0gi1r1b5enqhqx3orltpb02
       foreign key (userId)
       references app_user;
